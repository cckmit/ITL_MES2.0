package com.itl.mes.core.provider.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.mes.core.api.dto.DeviceDto;
import com.itl.mes.core.api.dto.DispatchDTO;
import com.itl.mes.core.api.entity.Device;
import com.itl.mes.core.api.entity.Dispatch;
import com.itl.mes.core.api.entity.OperationOrder;
import com.itl.mes.core.api.entity.Sfc;
import com.itl.mes.core.api.service.DeviceService;
import com.itl.mes.core.api.service.DispatchService;
import com.itl.mes.core.api.service.SfcService;
import com.itl.mes.core.api.vo.DispatchVo;
import com.itl.mes.core.provider.mapper.DispatchMapper;
import com.itl.mes.core.provider.mapper.OperationMapper;
import com.itl.mes.core.provider.mapper.SfcMapper;
import com.itl.mes.core.provider.service.impl.DispatchServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/dispatch")
@Api(tags = "工单工序任务派工" )
public class DispatchController {
    @Autowired
    private DispatchService dispatchService;

    @Autowired
    private DispatchMapper dispatchMapper;

    @Autowired
    private OperationMapper operationMapper;

    @Resource
    private UserUtil userUtil;

    @Autowired
    private SfcMapper sfcMapper;

    @Autowired
    private DispatchServiceImpl dispatchServiceImpl;

    /**
     * 确认派工查询列表
     * @return
     */
    @PostMapping("/okDispatchList")
    @ApiOperation(value="确认派工查询列表")
    public ResponseData<List<Dispatch>> okDispatchList(@RequestBody DispatchDTO dispatchDTO){
        return ResponseData.success(dispatchService.okDispatchList(dispatchDTO));
    }

    /**
     * 确认派工
     * @return
     */
    @PostMapping("/okDispatch")
    @ApiOperation(value="确认派工")
    public ResponseData<Dispatch> okDispatch(@RequestBody DispatchDTO dispatchDTO) throws CommonException {
        return ResponseData.success(dispatchService.okDispatch(dispatchDTO));
    }

    @PostMapping("/editDispatch")
    @ApiOperation(value = "改派")
    public ResponseData<Dispatch> editDispatch(@RequestBody DispatchDTO dispatchDTO) throws CommonException{
        Dispatch dispatch = dispatchMapper.selectOne(new QueryWrapper<Dispatch>().eq("id", dispatchDTO.getId()));
        BigDecimal sfcQtyTotal = dispatchServiceImpl.getSfcQtyTotal(dispatch.getDispatchCode());
        BigDecimal waitInQty = dispatch.getDispatchQty().subtract(sfcQtyTotal);
        BigDecimal rangeMin = dispatch.getDispatchQty().subtract(waitInQty);
        BigDecimal rangeMax = dispatch.getDispatchQty();
        boolean rangeInside = dispatchServiceImpl.isRangeInside(rangeMin,rangeMax, dispatchDTO.getDispatchQty());
        if (!rangeInside){
            throw new CommonException("派工数量改派应在："+rangeMin.toString()+"——"+rangeMax.toString()+"之间",504);
        }
        BigDecimal diffQty = dispatch.getDispatchQty().subtract(dispatchDTO.getDispatchQty());//改派的差异数
        dispatch.setDispatchQty(dispatchDTO.getDispatchQty());
        dispatch.setWaitIn(dispatch.getWaitIn().subtract(diffQty));
        dispatch.setCanPrintQty(dispatchDTO.getDispatchQty());
        dispatchMapper.updateById(dispatch);
        return ResponseData.success(dispatch);
    }

    /**
     * 根据工序bo查询所有的设备列表
     * @param
     * @return
     */
    @ApiOperation(value="根据工序bo查询所有的设备列表")
    @PostMapping("/selectDeviceByOperation")
    public ResponseData<IPage<Device>> selectDeviceByOperation(@RequestBody DeviceDto deviceDto){
        return ResponseData.success(dispatchService.pageDevice(deviceDto));

    }
    /**
     * 查询所有的工序任务
     */
    @ApiOperation(value="查询所有的工序任务")
    @PostMapping("/queryDispatchTask")
    public ResponseData<IPage<Dispatch>> queryDispatchTask(@RequestBody DispatchDTO dispatchDTO){
        if (ObjectUtil.isEmpty(dispatchDTO.getPage())){
            dispatchDTO.setPage(new Page(1,10));
        }
        IPage<Dispatch> dispatchIPage = dispatchMapper.queryAllDispatchTask(dispatchDTO.getPage(), dispatchDTO);
        List<String> sfcList;
        for (Dispatch record : dispatchIPage.getRecords()) {
            //把派工数量为-1的数据设置为空
            if (record.getDispatchQty().toString().equals("-1")){
                record.setDispatchQty(null);
            }
            dispatchDTO.setDispatchCode(record.getDispatchCode());
            //根据派工单查询sfc
            sfcList = dispatchMapper.selectSfcByDispatchCode(dispatchDTO);
            // 根据工单BO查询BOM详情
            record.setBomMapList(dispatchService.selectBomDetail(record.getShopOrderBo()));
            record.setSfcList(sfcList);
        }
        return ResponseData.success(dispatchIPage);
    }

    @ApiOperation(value = "查询所有可以打印的派工单")
    @PostMapping("/getAllCanPrintDispatch")
    public ResponseData<IPage<DispatchVo>> getAllCanPrintDispatch(@RequestBody DispatchDTO dispatchDTO) throws CommonException{
        // 如果是管理员账号则不用查询车间
        if(!userUtil.getUser().getUserName().equals("admin")){
            String workShop = operationMapper.selectWorkShopByUserName(userUtil.getUser().getUserName());
            dispatchDTO.setWorkShop(workShop);
        }
        IPage<DispatchVo> allCanPrintDispatch = dispatchMapper.getAllCanPrintDispatch(dispatchDTO.getPage(), dispatchDTO);
//        if(allCanPrintDispatch.getRecords()==null || allCanPrintDispatch.getRecords().size()==0){
//            throw new CommonException("您没有权限查看该派工单信息",30002);
//        }
        for (DispatchVo dispatch : allCanPrintDispatch.getRecords()) {
            if (StringUtils.isNotBlank(dispatch.getDevice())){
                dispatch.setDispatchCodeQty(dispatch.getDispatchQty());
            }else {
                dispatch.setDispatchCodeQty(dispatch.getOperationOrderQty());
            }
        }
        return ResponseData.success(allCanPrintDispatch);
    }
}
