package com.itl.iap.report.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.report.api.dto.SfcNcLogReportDto;
import com.itl.iap.report.api.dto.SfcScrapDto;
import com.itl.iap.report.api.dto.SfcScrapListDto;
import com.itl.iap.report.api.entity.SfcNcLog;
import com.itl.iap.report.api.entity.SfcScrap;

import java.util.List;
import java.util.Set;

public interface SfcNcLogReportService extends IService<SfcNcLog> {
    IPage<SfcNcLog> queryList(SfcNcLogReportDto sfcNcLogReportDto);
    IPage<SfcScrap> querySfcScrap(SfcScrapDto sfcScrapDto);


    Set<String> insScrap(List<SfcScrapListDto> sfcScrapListDto);
}
