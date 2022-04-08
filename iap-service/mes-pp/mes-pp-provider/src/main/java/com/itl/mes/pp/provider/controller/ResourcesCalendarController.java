package com.itl.mes.pp.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.pp.api.dto.scheduleplan.*;
import com.itl.mes.pp.api.entity.scheduleplan.ResourcesCalendarEntity;
import com.itl.mes.pp.api.service.ResourcesCalendarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("资源日历控制层")
@RestController
@RequestMapping("/p/resourcesCalendar")
public class ResourcesCalendarController {


    @Autowired
    ResourcesCalendarService resourcesCalendarService;

    @ApiOperation(value = "查询车间下的所有资源", notes = "查询车间下的所有资源", httpMethod = "GET")
    @GetMapping(value = "/workShopUnderResources/{workShopBo}")
    public ResponseData<List<WorkShopUnderResourcesRespDTO>> workShopUnderResources(@PathVariable String workShopBo) {
        return ResponseData.success(resourcesCalendarService.workShopUnderResources(workShopBo));
    }

    @ApiOperation(value = "查询班制班次信息", notes = "查询班制班次信息", httpMethod = "GET")
    @GetMapping(value = "/classSystemFrequencys")
    public ResponseData<List<ResourcesCalendarClassRespDTO>> classSystemFrequencys(){
        return ResponseData.success(resourcesCalendarService.classResources());
    }


    @ApiOperation(value = "生成资源日历", notes = "生成资源日历", httpMethod = "POST")
    @PostMapping(value = "/createResourcesCalendars")
    public ResponseData<List<ResourcesCalendarRespDTO>> createResourcesCalendars(@RequestBody CreateResourcesCalendarDTO createResourcesCalendarDTO) {

        return ResponseData.success(resourcesCalendarService.createResourcesCalendars(createResourcesCalendarDTO));
    }

    @ApiOperation(value = "批量保存", notes = "批量保存", httpMethod = "PUT")
    @PutMapping(value = "/batchSave")
    public ResponseData batchSave(@RequestBody List<ResourcesCalendarEntity> resourcesCalendarEntities){

        try {
            resourcesCalendarService.batchSave(resourcesCalendarEntities);
            return ResponseData.success(true);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseData.error(e.getMessage());
        }


    }

    @ApiOperation(value = "修改", notes = "修改", httpMethod = "PUT")
    @PutMapping(value = "/update")
    public ResponseData update(@RequestBody ResourcesCalendarEntity resourcesCalendarEntity){
        resourcesCalendarService.update(resourcesCalendarEntity);
        return ResponseData.success(true);
    }




    @ApiOperation(value = "查询资源日历信息", notes = "查询资源日历信息", httpMethod = "POST")
    @PostMapping(value = "/list")
    public ResponseData<ResourcesCalendarGatherRespDTO> list(@RequestBody ResourcesCalendarQueryDTO resourcesCalendarQueryDTO){

        return ResponseData.success(resourcesCalendarService.resourcesCalendars(resourcesCalendarQueryDTO));

    }

    @ApiOperation(value = "复制资源日历信息", notes = "复制资源日历信息", httpMethod = "POST")
    @PostMapping(value = "/copyResourcesCalendars")
    public ResponseData copyResourcesCalendars(@RequestBody ResourceCopySaveDTO resourceCopySaveDTO) throws Exception {

        try {
            resourcesCalendarService.copyResourcesCalendars(resourceCopySaveDTO);
            return ResponseData.success(true);
        }catch (Exception e){
            return ResponseData.error(e.getMessage());
        }

    }



    @PostMapping(value = "/getResourcesCalendarByTerm")
    public List<ResourcesCalendarEntity> getResourcesCalendarByTerm(@RequestBody ResourcesCalendarFeignQueryDTO resourcesCalendarFeignQueryDTO){

        return resourcesCalendarService.getResourcesCalendarByTerm(resourcesCalendarFeignQueryDTO);

    }

}
