package com.itl.mes.core.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.OpThroughRateDTO;
import com.itl.mes.core.api.service.MesReportService;
import com.itl.mes.core.api.vo.OpThroughRateVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "mes报表控制层")
@RestController
@RequestMapping("/mesReport")
public class MesReportController {

    @Autowired
    private MesReportService mesReportService;

    /**
     * 工序直通率报表
     */
    @ApiOperation(value = "工序直通率报表")
    @PostMapping("/operationThroughRate")
    public ResponseData<IPage<OpThroughRateVo>> operationThroughRateReport(@RequestBody OpThroughRateDTO opThroughRateDTO){
        return ResponseData.success(mesReportService.getOperationThroughRateReport(opThroughRateDTO));
    }
}
