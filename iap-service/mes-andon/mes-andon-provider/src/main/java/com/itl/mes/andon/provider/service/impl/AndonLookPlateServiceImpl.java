package com.itl.mes.andon.provider.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.andon.api.dto.AndonLookPlateQueryDTO;
import com.itl.mes.andon.api.dto.RecordUpdateDTO;
import com.itl.mes.andon.api.entity.Record;
import com.itl.mes.andon.api.service.AndonLookPlateService;
import com.itl.mes.andon.api.vo.AndonLookPlateQueryVo;
import com.itl.mes.andon.api.vo.AndonLookPlateVo;
import com.itl.mes.andon.api.vo.RecordVo;
import com.itl.mes.andon.provider.common.CommonUtils;
import com.itl.mes.andon.provider.config.Constant;
import com.itl.mes.andon.provider.mapper.AndonLookPlateMapper;
import com.itl.mes.andon.provider.mapper.AndonTriggerMapper;
import com.itl.mes.andon.provider.mapper.RecordMapper;
import com.itl.mes.pp.api.dto.scheduleplan.ResourcesCalendarFeignQueryDTO;
import com.itl.mes.pp.api.entity.scheduleplan.ResourcesCalendarEntity;
import com.itl.pp.core.client.PpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @auth liuchenghao
 * @date 2020/12/27
 */
@Service
public class AndonLookPlateServiceImpl implements AndonLookPlateService {

    @Autowired
    AndonLookPlateMapper andonLookPlateMapper;

    @Autowired
    AndonTriggerMapper andonTriggerMapper;


    @Autowired
    RecordMapper recordMapper;

    @Autowired
    PpService ppService;

    @Override
    public List<AndonLookPlateVo> getAndonLookPlate(AndonLookPlateQueryDTO andonLookPlateQueryDTO) {

        andonLookPlateQueryDTO.setSite(UserUtils.getSite());
        List<AndonLookPlateQueryVo> andonLookPlateQueryVos;
        //车间有值则资源信息显示工位，没有则显示产线
        if(StrUtil.isNotBlank(andonLookPlateQueryDTO.getWorkShopBo())){
            andonLookPlateQueryVos = andonLookPlateMapper.findStationAndonLookPlate(andonLookPlateQueryDTO);
        }else {
            andonLookPlateQueryVos= andonLookPlateMapper.findProductLineAndonLookPlant(andonLookPlateQueryDTO);
        }
        List<AndonLookPlateVo> andonLookPlateVos = new ArrayList<>();
        Set<String> bos = new HashSet<>();
        andonLookPlateQueryVos.forEach(andonLookPlateQueryVo -> {
            if(bos.contains(andonLookPlateQueryVo.getBo())){
                return;
            }
            AndonLookPlateVo andonLookPlateVo = new AndonLookPlateVo();
            andonLookPlateVo.setResourceName(andonLookPlateQueryVo.getResourceName());
            andonLookPlateVo.setIsAbnormal(false);
            List<String> deviceAbnormal = new ArrayList<>();
            List<String> itemAbnormal = new ArrayList<>();
            List<String> qualityAbnormal = new ArrayList<>();
            andonLookPlateQueryVos.forEach(lookPlateVo ->{
                if(lookPlateVo.getBo().equals(andonLookPlateQueryVo.getBo()) && StrUtil.isNotBlank(lookPlateVo.getPid())){
                    if(lookPlateVo.getState().equals(Constant.recordState.TRIGGER.getValue())){
                        andonLookPlateVo.setIsAbnormal(true);
                        switch (lookPlateVo.getAndonResourceType()){
                            case Constant.resourceTypeDevice:
                                deviceAbnormal.add(lookPlateVo.getPid());
                                break;
                            case Constant.resourceTypeItem:
                                itemAbnormal.add(lookPlateVo.getPid());
                                break;
                            default:break;
                        }
                    }else {
                        return;
                    }

                }else {
                    return;
                }
            });
            andonLookPlateVo.setDeviceAbnormal(deviceAbnormal);
            andonLookPlateVo.setItemAbnormal(itemAbnormal);
            andonLookPlateVo.setQualityAbnormal(qualityAbnormal);
            andonLookPlateVos.add(andonLookPlateVo);
            bos.add(andonLookPlateQueryVo.getBo());

        });

        return andonLookPlateVos;
    }

    @Override
    public RecordVo getRecordById(String pid) {

        Record record = recordMapper.selectById(pid);
        RecordVo recordVo = new RecordVo();
        BeanUtil.copyProperties(record,recordVo);
        if(StrUtil.isNotBlank(recordVo.getAbnormalImg())){
            recordVo.setImgs(CommonUtils.convertImgToStringList(recordVo.getAbnormalImg()));
        }
        return recordVo;
    }

    @Override
    public void updateRecord(RecordUpdateDTO recordUpdateDTO) {
        Record record = new Record();
        record.setState(Constant.recordState.REPAIT.getValue());
        BeanUtil.copyProperties(recordUpdateDTO,record);
        String userName = UserUtils.getCurrentUser().getUserName();
        record.setReceiveMan(userName);
        Date date = new Date();
        record.setReceiveTime(date);
        record.setCloseMan(userName);
        record.setCloseTime(date);

        //统计出影响的工单
        List<Map<String,String>> stationList = andonTriggerMapper.getStationList(UserUtils.getCurrentUser().getId(),UserUtils.getSite());
        String stationBo = stationList.get(0).get("stationBo");
        String productionBo = andonTriggerMapper.getProductLineBo(stationBo);
        //根据产线查询工单信息
        List<String> shopOrders = andonLookPlateMapper.getShopOrdersByProductLineBo(productionBo);
        if(CollUtil.isNotEmpty(shopOrders)){
            String shopOrderArr = "";
            for(String shopOrder:shopOrders){
                shopOrderArr += shopOrder+",";
                if(shopOrders.get(shopOrders.size()-1).equals(shopOrder)){
                    shopOrderArr+=shopOrder;
                }
            }
            record.setAffectShopOrder(shopOrderArr);
        }
        //计算异常工时
        Record queryRecord = recordMapper.selectById(recordUpdateDTO.getPid());
        ResourcesCalendarFeignQueryDTO resourcesCalendarFeignQueryDTO = new ResourcesCalendarFeignQueryDTO();
        resourcesCalendarFeignQueryDTO.setBeginDate(queryRecord.getTriggerTime());
        resourcesCalendarFeignQueryDTO.setEndDate(date);
        resourcesCalendarFeignQueryDTO.setProductLineBo(productionBo);
        List<ResourcesCalendarEntity> resourcesCalendars = ppService.getResourcesCalendarByTerm(resourcesCalendarFeignQueryDTO);
        if(CollUtil.isNotEmpty(resourcesCalendars)){
            Double abnormalWorkHour = 0.0;
            for(ResourcesCalendarEntity resourcesCalendarEntity: resourcesCalendars){
                abnormalWorkHour += resourcesCalendarEntity.getWorkHour();
            }
            record.setAbnormalWorkHour(abnormalWorkHour);
        }
        recordMapper.updateById(record);

    }


}
