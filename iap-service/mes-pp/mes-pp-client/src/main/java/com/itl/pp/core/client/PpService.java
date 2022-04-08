package com.itl.pp.core.client;


import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.pp.api.dto.scheduleplan.ResourcesCalendarFeignQueryDTO;
import com.itl.mes.pp.api.entity.schedule.ScheduleEntity;
import com.itl.mes.pp.api.entity.scheduleplan.ResourcesCalendarEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "mes-pp-provider")
public interface PpService {

    @PostMapping(value = "/p/resourcesCalendar/getResourcesCalendarByTerm")
    List<ResourcesCalendarEntity> getResourcesCalendarByTerm(@RequestBody ResourcesCalendarFeignQueryDTO resourcesCalendarFeignQueryDTO);

    /**
     * 根据排程BO查询排程
     */
    @GetMapping(value = "/p/schedule/getByc/process")
    ResponseData<ScheduleEntity> getByScheduleBoProcess(@RequestParam("scheduleBo") String scheduleBo);

}
