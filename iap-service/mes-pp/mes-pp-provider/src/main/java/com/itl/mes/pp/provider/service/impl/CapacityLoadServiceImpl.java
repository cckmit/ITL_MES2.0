package com.itl.mes.pp.provider.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.itl.mes.pp.api.dto.CapacityLoadQueryDTO;
import com.itl.mes.pp.api.dto.CapacityLoadReportDTO;
import com.itl.mes.pp.api.dto.CapacityLoadRespDTO;
import com.itl.mes.pp.api.service.CapacityLoadService;
import com.itl.mes.pp.provider.mapper.CapacityLoadMapper;
import com.itl.mes.pp.provider.mapper.ResourcesCalendarMapper;
import com.itl.mes.pp.provider.util.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @auth liuchenghao
 * @date 2021/1/7
 */
@Service
public class CapacityLoadServiceImpl implements CapacityLoadService {

    @Autowired
    CapacityLoadMapper capacityLoadMapper;

    @Autowired
    ResourcesCalendarMapper resourcesCalendarMapper;

    @Override
    public CapacityLoadReportDTO findCapacityLoadList(CapacityLoadQueryDTO capacityLoadQueryDTO) {

        CapacityLoadReportDTO capacityLoadReportDTO = new CapacityLoadReportDTO();
        //根据时间范围取时间
        List<String> dateList = new ArrayList<>();
        String nextDay = "";
        String startDate = "";
        String endDate = "";
        for(int i = 0;i<capacityLoadQueryDTO.getDateRange();i++){
            if(i!=0){
                dateList.add(nextDay);
                if(i==capacityLoadQueryDTO.getDateRange()-1){
                    endDate = nextDay;
                }else {
                    nextDay = TimeUtils.getAfterTime(nextDay+" 00:00:00", (double) 24);
                }

            }else {
                dateList.add(DateUtil.today());
                startDate = DateUtil.today();
                nextDay = TimeUtils.getAfterTime(DateUtil.today()+" 00:00:00", (double) 24);

            }
        }
        //取机台产能额定开机时间和机台信息
        List<CapacityLoadRespDTO> machineAndRatedTimes = capacityLoadMapper.machineRatedTime("1040", capacityLoadQueryDTO);

        //取时间段的负荷报表信息
        List<CapacityLoadRespDTO> capacityLoadRespDTOList = capacityLoadMapper.capacityLoadList("1040",startDate,endDate);
        Set<String> productLineList = new HashSet<>();
        machineAndRatedTimes.forEach(machineAndRatedTime -> {
            capacityLoadRespDTOList.forEach(capacityLoadRespDTO -> {
                    if(machineAndRatedTime.getProductLine().equals(capacityLoadRespDTO.getProductLine())){
                        if(ObjectUtil.isNotEmpty(machineAndRatedTime.getRatedTime())){
                            if(machineAndRatedTime.getRatedTime()<capacityLoadRespDTO.getAvailableWorkHour()){

                                capacityLoadRespDTO.setAvailableWorkHour(capacityLoadRespDTO.getRatedTime());
                            }
                        }
                        Double surplusWorkHour = capacityLoadRespDTO.getAvailableWorkHour()-capacityLoadRespDTO.getArrangedWorkHour();
                        surplusWorkHour =(double) Math.round(surplusWorkHour * 100) / 100;
                        capacityLoadRespDTO.setSurplusWorkHour(surplusWorkHour);
                    }
            });
            productLineList.add(machineAndRatedTime.getProductLine());
        });
        List<Date> dates = new ArrayList<>();
        dateList.forEach(dateStr ->{
            dates.add(DateUtil.parse(dateStr));
        });

        capacityLoadReportDTO.setDateList(dates);
        capacityLoadReportDTO.setProductLines(productLineList);
        capacityLoadReportDTO.setCapacityLoadRespDTOList(capacityLoadRespDTOList);
        return capacityLoadReportDTO;
    }


}
