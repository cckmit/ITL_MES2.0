package com.itl.iap.report.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.report.api.dto.AndonDto;
import com.itl.iap.report.api.dto.AndonExceptionDto;
import com.itl.iap.report.api.dto.AndonParamDto;
import com.itl.iap.report.api.dto.AndonWarningDto;
import com.itl.iap.report.api.service.AndonReportService;
import com.itl.iap.report.api.vo.AndonTypeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/andonReport")
@Api(tags = "安灯看板" )
public class AndonReportController {
    @Autowired
    private AndonReportService andonReportService;

    @GetMapping("/query")
    @ApiOperation("查询andon主页面看板数据")
    public ResponseData<AndonDto> query(){
        return ResponseData.success(andonReportService.selectCountInfo());
    }

    @PostMapping("/queryWarning")
    @ApiOperation("andon预警看板数据")
    public ResponseData<AndonWarningDto> queryWarning(@RequestBody AndonParamDto andonParamDto){
        return ResponseData.success(andonReportService.selectAndonWarning(andonParamDto));
    }

    @PostMapping("/queryAndonType")
    @ApiOperation("异常类型分析报表")
    public ResponseData<List<AndonTypeVo>> queryAndonType(@RequestBody AndonParamDto andonParamDto){
        return ResponseData.success(andonReportService.selectAndonType(andonParamDto));
    }
    @PostMapping("/queryAndonAllTime")
    @ApiOperation("异常时长汇总报表")
    public ResponseData<List<AndonTypeVo>> selectAndonAllTime(@RequestBody AndonParamDto andonParamDto){
        return ResponseData.success(andonReportService.selectAndonAllTime(andonParamDto));
    }

    @PostMapping("queryAndonException")
    @ApiOperation("安灯统计报表")
    public ResponseData<AndonExceptionDto> queryAndonException(@RequestBody AndonParamDto andonParamDto){
        return ResponseData.success(andonReportService.selectAndonExceptionInfo(andonParamDto));
    }
}
