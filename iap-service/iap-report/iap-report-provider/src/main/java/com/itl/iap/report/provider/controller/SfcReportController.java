package com.itl.iap.report.provider.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.response.ResponseData;

import com.itl.iap.report.api.dto.SfcReportDto;
import com.itl.iap.report.api.entity.Sfc;
import com.itl.iap.report.api.service.SfcReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/sfcReport")
@Api(tags = "sfc报表" )
public class SfcReportController {

    @Autowired
    public SfcReportService sfcReportService;

    @PostMapping("/list")
    @ApiOperation("分页查询sfc报表信息")
    public ResponseData<IPage<Sfc>>list(@RequestBody SfcReportDto sfcReportDto){
        return ResponseData.success(sfcReportService.queryList(sfcReportDto));
    }
}
