package com.itl.mes.core.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.SfcDataStatisticsDto;
import com.itl.mes.core.api.service.QualityKanbanService;
import com.itl.mes.core.api.vo.SfcDataStatisticsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "品质看板")
@RestController
@RequestMapping("/qualityKanban")
public class QualityKanbanController {

    @Autowired
    private QualityKanbanService qualityKanbanService;

    @PostMapping("/getSfcDataStatistics")
    @ApiOperation("sfc完成数据统计")
    public ResponseData<IPage<SfcDataStatisticsVo>> getSfcDataStatistics(@RequestBody SfcDataStatisticsDto sfcDataStatisticsDto){
        return ResponseData.success(qualityKanbanService.getSfcDataStatistics(sfcDataStatisticsDto));
    }
}
