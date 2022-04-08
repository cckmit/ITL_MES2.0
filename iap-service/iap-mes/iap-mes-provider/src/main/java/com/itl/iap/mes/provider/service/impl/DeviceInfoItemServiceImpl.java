package com.itl.iap.mes.provider.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.mes.api.dto.DeviceInfoItemDto;
import com.itl.iap.mes.api.entity.CheckPlan;
import com.itl.iap.mes.api.entity.DataCollectionItem;
import com.itl.iap.mes.api.entity.UpkeepPlan;
import com.itl.iap.mes.api.service.DeviceInfoItemService;
import com.itl.iap.mes.api.vo.CheckPlanVo;
import com.itl.iap.mes.api.vo.DeviceInfoItemVo;
import com.itl.iap.mes.provider.mapper.CheckPlanMapper;
import com.itl.iap.mes.provider.mapper.DataCollectionItemMapper;
import com.itl.iap.mes.provider.mapper.UpkeepPlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class DeviceInfoItemServiceImpl implements DeviceInfoItemService {
    @Autowired
    private CheckPlanMapper checkPlanMapper;

    @Autowired
    private DataCollectionItemMapper dataCollectionItemMapper;

    @Autowired
    private UpkeepPlanMapper upkeepPlanMapper;

    @Override
    public DeviceInfoItemVo query(DeviceInfoItemDto param) {
        String deviceType=param.getDeviceType();
        List<DeviceInfoItemVo> deviceInfoItemVos=new ArrayList<>();
        CheckPlan checkPlan=new CheckPlan();
        if(deviceType.contains(",")){
            //根据逗号分割设备类型
            String[] deviceTypes=deviceType.split(",");
            //根据设备类型查询该类型最近创建的数据收集组
            for(int i=0;i<deviceTypes.length;i++){
                //点检信息
                checkPlan=checkPlanMapper.selectByDeviceType(deviceTypes[i]);
                if(checkPlan !=null && StrUtil.isNotEmpty(checkPlan.getId())){
                    DeviceInfoItemVo deviceInfoItemVo=new DeviceInfoItemVo();
                    //日点检存入周期类型为0
                    deviceInfoItemVo.setType("0");
                    //查询数据收集组信息
                    List<DataCollectionItem>  dataCollectionItems=dataCollectionItemMapper.selectList(new QueryWrapper<DataCollectionItem>().eq("dataCollectionId",checkPlan.getDataCollectionId()));
                    deviceInfoItemVo.setDataCollectionItems(dataCollectionItems);
                    deviceInfoItemVos.add(deviceInfoItemVo);
                }
                //保养信息
                List<UpkeepPlan> upkeepPlans=upkeepPlanMapper.getByDeviceTypeWithOutupkeepPlanName(deviceTypes[i]);
                if(upkeepPlans != null){
                    for(UpkeepPlan upkeepPlan:upkeepPlans){
                        DeviceInfoItemVo deviceInfoItemVo=new DeviceInfoItemVo();
                        deviceInfoItemVo.setType(upkeepPlan.getUpkeepPlanName());
                        List<DataCollectionItem>  dataCollectionItems=dataCollectionItemMapper.selectList(new QueryWrapper<DataCollectionItem>().eq("dataCollectionId",upkeepPlan.getDataCollectionId()));
                        deviceInfoItemVo.setDataCollectionItems(dataCollectionItems);
                        deviceInfoItemVos.add(deviceInfoItemVo);
                    }
                }
            }
        }else{
            //点检信息
            checkPlan=checkPlanMapper.selectByDeviceType(deviceType);
            if(checkPlan !=null && StrUtil.isNotEmpty(checkPlan.getId())){
                DeviceInfoItemVo deviceInfoItemVo=new DeviceInfoItemVo();
                //日点检存入周期类型为0
                deviceInfoItemVo.setType("0");
                //查询数据收集组信息
                List<DataCollectionItem>  dataCollectionItems=dataCollectionItemMapper.selectList(new QueryWrapper<DataCollectionItem>().eq("dataCollectionId",checkPlan.getDataCollectionId()));
                deviceInfoItemVo.setDataCollectionItems(dataCollectionItems);
                deviceInfoItemVos.add(deviceInfoItemVo);
            }
            //保养信息
            List<UpkeepPlan> upkeepPlans=upkeepPlanMapper.getByDeviceTypeWithOutupkeepPlanName(deviceType);
            if(upkeepPlans !=null){
                for(UpkeepPlan upkeepPlan:upkeepPlans){
                    DeviceInfoItemVo deviceInfoItemVo=new DeviceInfoItemVo();
                    deviceInfoItemVo.setType(upkeepPlan.getUpkeepPlanName());
                    List<DataCollectionItem>  dataCollectionItems=dataCollectionItemMapper.selectList(new QueryWrapper<DataCollectionItem>().eq("dataCollectionId",upkeepPlan.getDataCollectionId()));
                    deviceInfoItemVo.setDataCollectionItems(dataCollectionItems);
                    deviceInfoItemVos.add(deviceInfoItemVo);
                }
            }
        }
        //根据分页截取子集合
        int page=Integer.parseInt(String.valueOf(param.getPage().getCurrent()));
        int size=Integer.parseInt(String.valueOf(param.getPage().getSize()));
        Map<String, DeviceInfoItemDto> map=new LinkedHashMap<>(); //key->数据项id，value->保存数据项实体和周期集合实体
        //遍历设备点检/保养项信息
        for(DeviceInfoItemVo deviceInfoItemVo:deviceInfoItemVos){
            List<DataCollectionItem> dataCollectionItems=deviceInfoItemVo.getDataCollectionItems();
            if(dataCollectionItems !=null){
                for(DataCollectionItem dataCollectionItem:dataCollectionItems){
                    //map是否存在该id
                    if(map.get(dataCollectionItem.getId()) == null){
                        DeviceInfoItemDto deviceInfoItemDto=new DeviceInfoItemDto();
                        //Set<String> set=new HashSet<>();
                       // set.add(deviceInfoItemVo.getType());
                        //deviceInfoItemDto.setTypeList(set);
                        if(deviceInfoItemVo.getType() !=null && deviceInfoItemVo.getType().equals("0")){
                            dataCollectionItem.setQuarter("0");
                        }else if(deviceInfoItemVo.getType() !=null && deviceInfoItemVo.getType().equals("1")){
                            dataCollectionItem.setWeek("1");
                        }else {
                            dataCollectionItem.setDay("2");
                        }
                        deviceInfoItemDto.setDataCollectionItem(dataCollectionItem);
                        map.put(dataCollectionItem.getId(),deviceInfoItemDto);
                    }else{
                        if(deviceInfoItemVo.getType() !=null && deviceInfoItemVo.getType().equals("0")){
                            map.get(dataCollectionItem.getId()).getDataCollectionItem().setQuarter("0");
                        }else if(deviceInfoItemVo.getType() !=null && deviceInfoItemVo.getType().equals("1")){
                            map.get(dataCollectionItem.getId()).getDataCollectionItem().setWeek("1");
                        }else {
                            map.get(dataCollectionItem.getId()).getDataCollectionItem().setDay("2");
                        }
                    }
                }
            }
        }
        //转为List集合
        List<DeviceInfoItemDto> resultList=new ArrayList<>();
        map.forEach((key,value) -> {
            resultList.add(value);
        });
        int fromIndex= (page-1)*size;
        int toIndex=page*size;
        if(toIndex>resultList.size()){
            toIndex=resultList.size();
        }
        List<DeviceInfoItemDto> subList=resultList.subList(fromIndex,toIndex);
        DeviceInfoItemVo resultVo=new DeviceInfoItemVo();
        resultVo.setPage(param.getPage());
        resultVo.getPage().setTotal(resultList.size());
        resultVo.getPage().setRecords(subList);
        return resultVo;
    }
}
