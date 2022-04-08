package com.itl.iap.report.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.report.api.service.OperationReportService;
import com.itl.iap.report.api.vo.OperationTestQualifiedRateVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "工序相关报表")
@RestController
@RequestMapping("/operationReport")
public class OperationReportController {

    @Autowired
    private OperationReportService operationReportService;

    @ApiOperation("工序检验合格率")
    @PostMapping("/listOperationTestQualifiedRate")
    public ResponseData<IPage<OperationTestQualifiedRateVo>> listOperationTestQualifiedRate(@RequestBody OperationTestQualifiedRateVo operationTestQualifiedRateVo){
        return ResponseData.success(operationReportService.getPageListOperationTestQualifiedRate(operationTestQualifiedRateVo));
    }
}
