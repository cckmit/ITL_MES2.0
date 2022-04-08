package com.itl.mes.core.provider.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.dto.PlmDto;
import com.itl.mes.core.api.entity.WorkStation;
import com.itl.mes.core.api.service.PlmService;
import com.itl.mes.core.provider.mapper.PlmMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PlmServiceImpl implements PlmService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PlmMapper plmMapper;

    @Override
    public void setErrMsg(String errMsg) {
        redisTemplate.opsForValue().set("errMsg", errMsg);
    }

    @Override
    public String getErrMsg() {
        Object obj=redisTemplate.opsForValue().get("errMsg");
        if(obj !=null){
            return obj.toString();
        }
        return null;
    }

    @Override
    public String getWorkStepNo(PlmDto plmDto) throws CommonException {
        String operationList=plmMapper.getOperationList(plmDto.getRouter(),plmDto.getVersion());
        if(operationList==null){
            throw new CommonException("PLM同步工艺路线数据存储表不存在该数据信息",504);
        }
        JSONArray jsonArray=JSONArray.parseArray(operationList);
        String stepNo=null;
        for(Object obj:jsonArray) {
            JSONObject jsonObject=JSON.parseObject(obj.toString());
            if(jsonObject.get("operation").toString().equals(plmDto.getOperation())){
                JSONArray jsonArraySteps=JSONArray.parseArray(jsonObject.get("workStepList").toString());
                for(Object object:jsonArraySteps){
                    JSONObject jsonStep=JSON.parseObject(object.toString());
                    if(jsonStep.get("workStepCode").toString().equals(plmDto.getWorkStepCode())){
                        stepNo = jsonStep.get("stepNo").toString();
                        return stepNo;
                    }
                }
            }
        }
        if(stepNo==null) {
            throw new CommonException("PLM同步工艺路线数据存储表不存在该数据信息",504);
        }
        return stepNo;
    }

    @Override
    public List<WorkStation> getAllWorkStation(String item, String version) throws CommonException {
        String site= UserUtils.getSite();
        // 根据item和version 获取工序步骤
        Map<String,String>  map=plmMapper.getProcessInfo(item,version);
        if(map==null){
            throw new CommonException("该数据对应工艺路线下的工序未创建，请检查",504);
        }
        JSONObject jsonObject=JSON.parseObject(map.get("process_info"));
        JSONArray jsonArray=JSONArray.parseArray(jsonObject.get("nodeList").toString());
        Map<String,WorkStation> stationMap=new HashMap<>();
        for(Object obj:jsonArray){
            Object operation=JSON.parseObject(obj.toString()).get("operation");
            if(operation !=null){
                List<WorkStation> workStationList= plmMapper.selectWorkStep(site,operation.toString());
                if(workStationList !=null && workStationList.size()>0){
                    for(WorkStation workStation:workStationList){
                        workStation.setRouter(map.get("router"));
                        workStation.setRouterVersion(map.get("routerVersion"));
                        if(stationMap.get(workStation.getBo())==null){
                            stationMap.put(workStation.getBo(),workStation);
                        }
                    }
                }
            }
        }
        List<WorkStation> workStationList=new ArrayList<>();
        stationMap.forEach((key,value)->{
            workStationList.add(value);
        });
        if(workStationList.size()==0){
            throw new CommonException("该条件下不存在工步信息",520);
        }
        return workStationList;
    }


}
