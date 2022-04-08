package com.itl.iap.report.provider.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.report.api.dto.SfcNcLogReportDto;
import com.itl.iap.report.api.dto.SfcScrapDto;
import com.itl.iap.report.api.dto.SfcScrapListDto;
import com.itl.iap.report.api.entity.SfcNcLog;
import com.itl.iap.report.api.entity.SfcScrap;
import com.itl.iap.report.api.service.SfcNcLogReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/sfcNcLogReport")
@Api(tags = "Sfc不合格记录报表")
public class SfcNcLogReportController {

    @Autowired
    public SfcNcLogReportService sfcNcLogReportService;


    @PostMapping("/list")
    @ApiOperation("分页查询Sfc不合格记录报表信息")
    public ResponseData<IPage<SfcNcLog>> list(@RequestBody SfcNcLogReportDto sfcNcLogReportDto) {
        return ResponseData.success(sfcNcLogReportService.queryList(sfcNcLogReportDto));
    }

    @PostMapping("/scrapList")
    @ApiModelProperty("报废数据")
    public ResponseData<IPage<SfcScrap>> queryScrap(@RequestBody SfcScrapDto sfcScrapDto) {
        return ResponseData.success(sfcNcLogReportService.querySfcScrap(sfcScrapDto));
    }

    @PostMapping("/throwERP")
    @ApiModelProperty("生成退料单")
    public ResponseData<Set<String>> throwERP(@RequestBody List<SfcScrapListDto> sfcScrapListDto) throws CommonException {
         // 前端每次复选框传递的都是一个dto对象(多个dto就是list)
        // System.out.println(sfcScrapListDto);

        if (null == sfcScrapListDto || sfcScrapListDto.size() == 0) {
            return ResponseData.error("500", "dto不存在");
        }
        for (SfcScrapListDto scrapListDto : sfcScrapListDto) {
            if (null == scrapListDto.getSfc()) {
                return ResponseData.error("500", "该mes入库物料sfc批次条码不存在");
            }
            if (null == scrapListDto.getBo()) {
                return ResponseData.error("500", "该bo未找到");
            }
        }

        // 调用sfcNcLogReportService的退料结果返回(前端解析set)
        Set<String> result = sfcNcLogReportService.insScrap(sfcScrapListDto);

        return ResponseData.success(result);
    }


}
