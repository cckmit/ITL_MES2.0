package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.util.UUID;
import com.itl.mes.core.api.dto.DeviceDto;
import com.itl.mes.core.api.dto.DispatchDTO;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.DeviceService;
import com.itl.mes.core.api.service.DispatchService;
import com.itl.mes.core.api.service.RouteStationService;
import com.itl.mes.core.api.service.RouterProcessService;
import com.itl.mes.core.api.vo.DeviceTypeSimplifyVo;
import com.itl.mes.core.provider.mapper.DispatchMapper;
import com.itl.mes.core.provider.mapper.OperationOrderMapper;
import com.itl.mes.core.provider.mapper.SfcMapper;
import com.itl.mes.core.provider.mapper.ShopOrderMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
public class DispatchServiceImpl extends ServiceImpl<DispatchMapper, Dispatch> implements DispatchService {
    @Autowired
    private DispatchMapper dispatchMapper;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private OperationOrderMapper operationOrderMapper;

    @Autowired
    private RouteStationService routeStationService;

    @Autowired
    private SfcServiceImpl sfcServiceImpl;

    @Autowired
    private ShopOrderMapper shopOrderMapper;

    @Autowired
    private SfcMapper sfcMapper;
    @Override
    public List<Dispatch> okDispatchList(DispatchDTO dispatchDTO) {
        List<Dispatch> dispatchList = dispatchMapper.okDispatchList(dispatchDTO);
        //重新计算可改派数（wait_in数量,派工单数量-该派工单已经打印条码的数量）
        for (Dispatch dispatch : dispatchList) {
            BigDecimal sfcQtyTotal = getSfcQtyTotal(dispatch.getDispatchCode());
            dispatch.setWaitIn(dispatch.getDispatchQty().subtract(sfcQtyTotal));
        }
        return dispatchList;
    }

    public BigDecimal getSfcQtyTotal(String dispatchCode) {
        List<Sfc> sfcList = sfcMapper.selectList(new QueryWrapper<Sfc>().eq("dispatch_code",dispatchCode));
        BigDecimal sfcQtyTotal = BigDecimal.ZERO;
        for (Sfc sfc : sfcList) {
            sfcQtyTotal = sfcQtyTotal.add(sfc.getSfcQty());
        }
        return sfcQtyTotal;
    }

    /**
     * 判断某个参数是否在指定范围内
     * @param rangeMin
     * @param rangeMax
     * @param param
     * @return
     */
    public boolean isRangeInside(BigDecimal rangeMin,BigDecimal rangeMax,BigDecimal param){
        if (param.compareTo(rangeMin) != -1 && param.compareTo(rangeMax) != 1){
            return true;
        }
        return false;
    }
    @Override
    public Dispatch okDispatch(DispatchDTO dispatchDTO) throws CommonException{
        String count = dispatchMapper.getDispatchTotalCountByOpAndOrder(dispatchDTO.getOperationBo(), dispatchDTO.getOperationOrder());
        BigDecimal total;
        if (StringUtils.isNotBlank(count)){
            total = new BigDecimal(count).add(dispatchDTO.getDispatchQty());
        }else {
            total = BigDecimal.ZERO.add(dispatchDTO.getDispatchQty());
        }
        if (total.compareTo(dispatchDTO.getOperationOrderQty()) == 1){
            throw new CommonException("派工数量已经超出工序工单数量!",504);
        }
        Dispatch dispatch = new Dispatch();
        BeanUtils.copyProperties(dispatchDTO,dispatch);
        dispatch.setId(UUID.uuid32());
        dispatch.setRemark(dispatchDTO.getRemark());
        dispatch.setCreateDate(new Date());
        dispatch.setWaitIn(dispatchDTO.getDispatchQty());
        dispatch.setCreateUser(userUtil.getUser().getUserName());
        dispatch.setItemBo("ITEM:dongyin," + dispatchDTO.getItem() + ",1.0");
        String shopOrder = operationOrderMapper.selectOne(new QueryWrapper<OperationOrder>().eq("operation_order", dispatchDTO.getOperationOrder())).getShopOrder();
        dispatch.setShopOrderBo("SO:dongyin," + shopOrder);
        //生成派工单
        QueryWrapper<Dispatch> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("operation_order",dispatchDTO.getOperationOrder());
        queryWrapper.isNotNull("DISPATCH_CODE");
        queryWrapper.orderByDesc("CREATE_DATE");
        List<Dispatch> dispatchList = dispatchMapper.selectList(queryWrapper);
        int num;
        if (CollectionUtil.isNotEmpty(dispatchList)){
            String theLastDisCode = dispatchList.get(0).getDispatchCode();//当前工序工单最后一条派工单
            num = Integer.parseInt(theLastDisCode.substring(theLastDisCode.substring(0, theLastDisCode.lastIndexOf("-")).length() + 1));
        }else {
            num = 0;
        }
        dispatch.setDispatchCode(dispatchDTO.getOperationOrder() + "-" + (num+1));//生成派工单
        dispatch.setCanPrintQty(dispatchDTO.getDispatchQty());//可打印数量 = 派工数量

        String theFirstOp = getTheFirstOp(dispatchDTO.getOperationOrder());
        if (theFirstOp.equals(dispatchDTO.getOperationBo())){//如果是首工序
            dispatch.setIsFirstOperation("1");
        }
        dispatchMapper.insert(dispatch);
        return dispatch;
    }

    public String getTheFirstOp(String operationOrder) {
        OperationOrder operationOrderObj = operationOrderMapper.selectOne(new QueryWrapper<OperationOrder>().eq("operation_order",operationOrder));
        String processInfo = routeStationService.queryRouterOperation(operationOrderObj.getRoutre(),operationOrderObj.getVersion());
        JSONObject jsonObj = JSON.parseObject(processInfo);
        JSONArray nodeList = JSONArray.parseArray(jsonObj.getString("nodeList"));
        JSONArray lineList = JSONArray.parseArray(jsonObj.getString("lineList"));
        String startId = sfcServiceImpl.getStartId(nodeList);
        String nextId = sfcServiceImpl.getNextId(lineList, startId);
        String theFirstOp = sfcServiceImpl.getOperationById(nodeList,nextId);//首工序
        return theFirstOp;
    }


    @Override
    public IPage<Device> pageDevice(DeviceDto deviceDto) {
       /* QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("operation_bo",deviceDto.getOperationBo());
        if (StringUtil.isNotBlank(deviceDto.getDevice())){
            queryWrapper.eq("device",deviceDto.getDevice());
        }
       IPage<Device> page= deviceService.page(deviceDto.getPage(),queryWrapper);

        //根据设备编码查询该设备已分配的设备类型
        for(Device record:page.getRecords()){
            //拼接设备类型，以逗号分割
            try {
                List<DeviceTypeSimplifyVo> assignedDeviceTypeList=deviceService.getDeviceVoTypeByDevice(record.getDevice()).getAssignedDeviceTypeList();
                String str="";
                int i=0;
                for(DeviceTypeSimplifyVo deviceTypeSimplifyVo:assignedDeviceTypeList){
                    if(i==assignedDeviceTypeList.size()-1){
                        str+=deviceTypeSimplifyVo.getDeviceType();
                    }else{
                        str+=deviceTypeSimplifyVo.getDeviceType()+",";
                    }
                    i++;
                }
                record.setDeviceType(str);
            } catch (CommonException e) {
                e.printStackTrace();
            }
        }*/
        //return page;
        return deviceService.query(deviceDto);
    }

    @Override
    public  List<Map<String,String>> selectBomDetail(String shopOrderBo) {
        ShopOrder shopOrder=shopOrderMapper.selectById(shopOrderBo);
        String bomBo=shopOrder.getBomBo();
        //根据bomBo获取bom详情（物料编码+规格）
        List<Item> list=dispatchMapper.selectBomDetailByBomBo(bomBo);
        List<Map<String,String>> bomMapList = new ArrayList<>();
        for(Item item:list){
            Map<String,String> param=new HashMap<>();
            param.put("item",item.getItem());
            param.put("itemDesc",item.getItemDesc());
            param.put("itemName",item.getItemName());
            bomMapList.add(param);
        }
        return bomMapList;
    }
}
