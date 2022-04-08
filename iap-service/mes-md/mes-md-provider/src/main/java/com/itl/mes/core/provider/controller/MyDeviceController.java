package com.itl.mes.core.provider.controller;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.util.UUID;
import com.itl.mes.core.api.dto.MyDeviceDto;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.ItemService;
import com.itl.mes.core.api.service.SfcService;
import com.itl.mes.core.api.service.ShopOrderService;
import com.itl.mes.core.api.vo.MyDeviceInfoVo;
import com.itl.mes.core.api.vo.MyDeviceVo;
import com.itl.mes.core.provider.mapper.*;
import com.itl.mes.core.provider.service.impl.SfcServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/myDevice")
@Api(tags = "人员设备（生产执行）" )
public class MyDeviceController {
    @Autowired
    private MyDeviceMapper myDeviceMapper;
    @Autowired
    private SfcServiceImpl sfcServiceImpl;
    @Autowired
    private SfcWiplogMapper sfcWiplogMapper;
//    @Autowired
//    private SfcService sfcService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ShopOrderService shopOrderService;
//    @Autowired
//    private EnterMapper enterMapper;
    @Autowired
    private DispatchMapper dispatchMapper;
    @Autowired
    private SfcDeviceMapper sfcDeviceMapper;
    /**
     * 根据用户id和工位bo查询人员设备
     * @param myDeviceDto
     * @return
     */
    @ApiOperation(value="根据用户id和工位bo查询人员设备信息")
    @PostMapping("/queryMyDevice")
    public ResponseData<List<MyDeviceVo>> queryMyDevice(@RequestBody MyDeviceDto myDeviceDto) throws CommonException {
        String operationBo=sfcServiceImpl.getUserInfo().getOperationBo();
        List<MyDeviceVo> myDeviceVoList=myDeviceMapper.selectMyDevice(myDeviceDto);
        for(MyDeviceVo myDeviceVo:myDeviceVoList){
            List<DeviceType> deviceTypeList= myDeviceMapper.selectDeviceTypeByDeviceBo(myDeviceVo.getDeviceBo());
            if(deviceTypeList !=null && deviceTypeList.size()>0){
                myDeviceVo.setDeviceType(deviceTypeList.get(deviceTypeList.size()-1).getDeviceType());
            }
            myDeviceVo.setDeviceTypeList(deviceTypeList);
            //查询当前设备上挂的工单的工单进度
            SfcDevice sfcDevice = sfcDeviceMapper.selectById(myDeviceVo.getDeviceBo());
            if (ObjectUtil.isNotEmpty(sfcDevice)){
                if (StringUtil.isNotEmpty(sfcDevice.getOperationOrder())){//如果设备上有工序工单在生产
                    ShopOrder shopOrderObj = shopOrderService.getById(sfcDevice.getShopOrderBo());
                    Item itemObj = itemService.getById(shopOrderObj.getItemBo());
                    //查询工单完成数(在当前工序)
                    QueryWrapper<SfcWiplog> wipLogQw = new QueryWrapper<>();
                    wipLogQw.eq("shop_order_bo",shopOrderObj.getBo());
                    wipLogQw.eq("operation_bo",operationBo);
                    List<SfcWiplog> sfcWipLogs = sfcWiplogMapper.selectList(wipLogQw);
                    BigDecimal totalQty = BigDecimal.ZERO;//当前工单在该工序完成的总数量
                    for (SfcWiplog sfcWiplog : sfcWipLogs) {
                        totalQty = totalQty.add(sfcWiplog.getDoneQty()).add(sfcWiplog.getScrapQty()).add(sfcWiplog.getNcQty());
                    }
                    if (shopOrderObj.getOrderQty().compareTo(totalQty) != 0){//当前还有工单没做完(进度并非百分百)
                        myDeviceVo.setShopOrder(shopOrderObj.getShopOrder());
                        myDeviceVo.setShopOrderQty(shopOrderObj.getOrderQty());
                        myDeviceVo.setItem(itemObj.getItem());
                        myDeviceVo.setItemDesc(itemObj.getItemDesc());
                        myDeviceVo.setOperationOrder(sfcDevice.getOperationOrder());
                        // 如果当前设备的工序工单不为空，则查询派工单信息
                        if(StringUtil.isNotEmpty(myDeviceVo.getOperationOrder())){
                            // 根据工序工单和当前设备查询
                            QueryWrapper<Dispatch> query=new QueryWrapper<>();
                            query.eq("OPERATION_ORDER",myDeviceVo.getOperationOrder());
                            query.eq("DEVICE",myDeviceVo.getDevice());
                            query.eq("OPERATION_BO",operationBo);
                            List<Dispatch> dispatchs=dispatchMapper.selectList(query);
                            if(dispatchs!=null && dispatchs.size()>0){
                                Dispatch dispatch=dispatchs.get(0);
                                myDeviceVo.setDispatchCode(dispatch.getDispatchCode());
                                if(dispatch.getDispatchQty() !=null){
                                    myDeviceVo.setDispatchQty(dispatch.getDispatchQty().intValue());
                                    myDeviceVo.setDispatchCodeQty(dispatch.getDispatchQty().intValue());
                                }
                            }
                        }
                        myDeviceVo.setDoneQty(totalQty);//完成数
                        myDeviceVo.setSfc(sfcDevice.getSfc());//在生产sfc,可以为空(首工序进站没有sfc)
                    }
                }
            }
        }
        return ResponseData.success(myDeviceVoList);
    }

    @ApiOperation(value="新增单条人员设备")
    @PostMapping("/insertMyDevice")
    public ResponseData<MyDevice> insertMyDevice(@RequestBody MyDeviceDto myDeviceDto) throws CommonException {
        //校验是否添加过设备
        QueryWrapper<MyDevice> qw = new QueryWrapper<>();
        qw.eq("USER_ID",myDeviceDto.getUserId());
        qw.eq("STATION_BO",myDeviceDto.getStationBo());
        qw.eq("DEVICE_BO",myDeviceDto.getDeviceBo());
        List<MyDevice> myDevices = myDeviceMapper.selectList(qw);
        if (!CollectionUtils.isEmpty(myDevices)){
            throw new CommonException("设备不能重复添加！", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        MyDevice myDevice = new MyDevice();
        BeanUtils.copyProperties(myDeviceDto,myDevice);
        myDevice.setId(UUID.uuid32());
        myDevice.setModifyTime(new Date());
        myDeviceMapper.insert(myDevice);
        return ResponseData.success(myDevice);
    }

    @ApiOperation(value="删除人员设备(支持批量删除)")
    @DeleteMapping("/deleteMyDevice")
    public ResponseData deleteMyDevice(@RequestBody List<MyDeviceDto> myDeviceDtos){
        myDeviceDtos.forEach(
                myDeviceDto -> {
                    myDeviceMapper.deleteById(myDeviceDto.getId());
                }
        );
        return ResponseData.success();
    }

    @ApiOperation(value="修改单条人员设备")
    @PostMapping("/updateMyDevice")
    public ResponseData<MyDevice> updateMyDevice(@RequestBody MyDeviceDto myDeviceDto){
        MyDevice myDevice = new MyDevice();
        myDevice.setId(myDeviceDto.getId());
        myDevice.setSpotState(myDeviceDto.getSpotState());
        myDevice.setFirstInsState(myDeviceDto.getFirstInsState());
        myDeviceMapper.updateById(myDevice);
        return ResponseData.success(myDevice);
    }

//    @GetMapping("/getMyDeviceInfo")
//    @ApiOperation("查询我的设备信息")
//    public ResponseData<MyDeviceInfoVo> getMyDeviceInfo(@RequestParam("sfc") String sfc){
//        Sfc sfcObj = sfcService.getOne(new QueryWrapper<Sfc>().eq("sfc", sfc));
//        ShopOrder shopOrder = shopOrderService.getById(sfcObj.getShopOrderBo());
//        Item itemObj = itemService.getById(sfcObj.getItemBo());
//        MyDeviceInfoVo myDeviceInfoVo = new MyDeviceInfoVo();
//        myDeviceInfoVo.setShopOrder(shopOrder.getShopOrder());
//        myDeviceInfoVo.setShopOrderQty(shopOrder.getOrderQty());
//        myDeviceInfoVo.setItem(itemObj.getItem());
//        myDeviceInfoVo.setItemDesc(itemObj.getItemDesc());
//        QueryWrapper<SfcWiplog> wiplogQueryWrapper = new QueryWrapper<>();
//        wiplogQueryWrapper.eq("shop_order_bo",sfcObj.getShopOrderBo());
//        wiplogQueryWrapper.eq("operation_bo",sfcServiceImpl.getUserInfo().getOperationBo());
//        List<SfcWiplog> sfcWiplogs = sfcWiplogMapper.selectList(wiplogQueryWrapper);
//        BigDecimal totalQty = BigDecimal.ZERO;//当前工单在该工序完成的总数量
//        for (SfcWiplog sfcWiplog : sfcWiplogs) {
//            totalQty = totalQty.add(sfcWiplog.getDoneQty()).add(sfcWiplog.getScrapQty()).add(sfcWiplog.getNcQty());
//        }
//        myDeviceInfoVo.setDoneQty(totalQty);
//        return ResponseData.success(myDeviceInfoVo);
//    }
}
