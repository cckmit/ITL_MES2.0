package com.itl.iap.report.provider.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.report.api.dto.CommonDto;
import com.itl.iap.report.api.dto.DeviceDto;
import com.itl.iap.report.api.dto.DeviceEfficientDto;
import com.itl.iap.report.api.dto.DeviceStateDto;
import com.itl.iap.report.api.service.DeviceReportService;
import com.itl.iap.report.api.vo.CommonVo;
import com.itl.iap.report.api.vo.DeviceStateVo;
import com.itl.iap.report.api.vo.EquipmentRunningRateVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/deviceReport")
@Api(tags = "设备看板" )
public class DeviceReportController {
    @Autowired
    private DeviceReportService deviceReportService;

    @GetMapping("/getEffientInfo")
    @ApiOperation("设备运行效率看板")
    public ResponseData<DeviceEfficientDto> getEffientInfo(){
        return ResponseData.success(deviceReportService.selectDeviceEfficient());
    }

    @PostMapping("/getRunningSate")
    @ApiOperation("设备运行状态看板")
    public ResponseData<List<CommonDto>> getRunningSate(@RequestBody DeviceDto deviceDto) throws ParseException {
        return ResponseData.success(deviceReportService.selectRunningSate(deviceDto));
    }

    @PostMapping("/deviceStateDetail")
    @ApiOperation("设备状态明细")
    public ResponseData<IPage<DeviceStateVo>>  deviceStateDetail(@RequestBody DeviceDto deviceDto){
        return  ResponseData.success(deviceReportService.deviceStateDetail(deviceDto));
    }

    @PostMapping("/deviceState")
    @ApiOperation("设备状态报表")
    public ResponseData<IPage<DeviceStateVo>> deviceState(@RequestBody DeviceDto deviceDto){
        return  ResponseData.success(deviceReportService.deviceState(deviceDto));
    }

    @PostMapping("/deviceDetail")
    @ApiOperation("设备详情")
    public ResponseData<IPage<DeviceStateVo>> deviceDetail(@RequestBody DeviceDto deviceDto){
        return  ResponseData.success(deviceReportService.deviceDetail(deviceDto));
    }

    @PostMapping("/DeviceObjectTime")
    @ApiOperation("设备稼动率统计")
    public ResponseData<PageInfo>  selectDeviceObjectTime(@RequestBody(required = false) EquipmentRunningRateVO equipmentRunningRateVO){
        return  ResponseData.success(deviceReportService.selectDeviceObjectTime(equipmentRunningRateVO));
    }

    @PostMapping("/DeviceObject")
    @ApiOperation("设备状态时长明细")
    public ResponseData<IPage<EquipmentRunningRateVO>>  selectDeviceObject(@RequestBody EquipmentRunningRateVO equipmentRunningRateVO){
        return  ResponseData.success(deviceReportService.selectDeviceObject(equipmentRunningRateVO));
    }

    @PostMapping("/selectStateInfo")
    @ApiOperation("设备详情")
    public ResponseData<DeviceStateDto> selectStateInfo(@RequestBody DeviceDto deviceDto){
        return  ResponseData.success(deviceReportService.selectStateInfo(deviceDto));
    }
}
