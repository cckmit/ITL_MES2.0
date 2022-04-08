package com.itl.mes.pp.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.pp.api.dto.schedule.*;
import com.itl.mes.pp.api.entity.schedule.ScheduleEntity;
import com.itl.mes.pp.api.service.ScheduleService;
import com.itl.mes.pp.provider.mapper.ScheduleMapper;
import feign.Param;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author liuchenghao
 * @date 2020/11/12 9:49
 */
@Api("排程管理控制层")
@RestController
@RequestMapping("/p/schedule")
public class ScheduleController {

    @Autowired
    ScheduleMapper scheduleMapper;

    @Autowired
    ScheduleService scheduleService;

    @PostMapping("/query")
    @ApiOperation(value = "分页查询信息", notes = "分页查询信息")
    public ResponseData<IPage<ScheduleRespDTO>> findList(@RequestBody ScheduleQueryDTO scheduleQueryDTO) {
        return ResponseData.success(scheduleService.findList(scheduleQueryDTO));
    }

    @ApiOperation(value = "save", notes = "新增/修改", httpMethod = "PUT")
    @PutMapping(value = "/save")
    public ResponseData save(@RequestBody ScheduleSaveDTO scheduleSaveDTO) {
        try {
            scheduleService.save(scheduleSaveDTO);
            return ResponseData.success(true);
        }catch (Exception e){
            return ResponseData.error(e.getMessage());
        }

    }

    @ApiOperation(value = "delete", notes = "删除", httpMethod = "DELETE")
    @DeleteMapping(value = "/delete")
    public ResponseData delete(@RequestBody List<String> ids)  {
        try {
            scheduleService.delete(ids);
            return ResponseData.success(true);
        } catch (CommonException e) {
            e.printStackTrace();
            return ResponseData.error(e.getMessage());
        }

    }

    @ApiOperation(value = "getById", notes = "查看一条", httpMethod = "GET")
    @GetMapping(value = "/getById/{id}")
    public ResponseData<ScheduleDetailRespDTO> getById(@PathVariable String id) {
        return ResponseData.success(scheduleService.findById(id));
    }

    @ApiOperation(value = "getByIdWithCount", notes = "查看一条WithCount", httpMethod = "GET")
    @GetMapping(value = "/getByIdWithCount/{id}")
    public ResponseData<ScheduleDetailRespDTO> getByIdWithCount(@PathVariable String id) {
        return ResponseData.success(scheduleService.findByIdWithCount(id));
    }



    @PostMapping("/release")
    @ApiOperation(value = "排程下达", notes = "排程下达")
    public ResponseData release(String scheduleBo) {
        scheduleService.updateState(scheduleBo);
        return ResponseData.success(true);
    }


    @PostMapping("/batchRelease")
    @ApiOperation(value = "批量排程下达", notes = "批量排程下达")
    public ResponseData batchRelease(@RequestBody List<String> scheduleBos) {
        scheduleService.batchRelease(scheduleBos);
        return ResponseData.success(true);
    }

    @PostMapping("/initScheduleProductionLineList")
    @ApiOperation(value = "查询初始化的排程产线，未保存数据查询显示", notes = "查询初始化的排程产线，未保存数据查询显示")
    public ResponseData<IPage<ProductionLineResDTO>> initScheduleProductionLineList(@RequestBody ScheduleProductionLineQueryDTO scheduleProductionLineQueryDTO) {

        return ResponseData.success(scheduleService.getInitScheduleProductionLineList(scheduleProductionLineQueryDTO));

    }

    @PostMapping("/getStationList")
    @ApiOperation(value = "查询产线工位", notes = "查询产线工位")
    public ResponseData<List<StationResDTO>> getStationList(String productionLineBo) {

        return ResponseData.success(scheduleService.getStationList(productionLineBo));

    }


    @PostMapping("/scheduleProductionLineList")
    @ApiOperation(value = "查询排程产线，保存数据后查询显示", notes = "查询排程产线，保存数据后查询显示")
    public ResponseData<IPage<ScheduleProductionLineRespDTO>> scheduleProductionLineList(@RequestBody ScheduleProductionLineQueryDTO scheduleProductionLineQueryDTO) {

        return ResponseData.success(scheduleService.getScheduleProductionLine(scheduleProductionLineQueryDTO));

    }

    @PostMapping("/getByProductionLine")
    public ResponseData<IPage<Map<String, Object>>> getByProductLine(@RequestBody ScheduleShowDto scheduleShowDto) {
        scheduleShowDto.setProductLine(
                Optional.ofNullable(scheduleShowDto.getProductLine())
                        .orElse(UserUtils.getProductLine())
        );
        return ResponseData.success(scheduleService.getByProductLine(scheduleShowDto));
    }


    /**
     * liKun
     * /p/schedule/getByc/process
     */
    @GetMapping("/getByc/process")
    @ApiOperation(value = "根据排程BO获取排程信息",notes = "仅供工序模块调用")
    public ResponseData<ScheduleEntity> getByScheduleBoProcess(@Param("scheduleBo") String scheduleBo) {
        ScheduleEntity scheduleEntity = scheduleMapper.selectById(scheduleBo);
        return ResponseData.success(scheduleEntity);
    }
}
