package com.itl.iap.report.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.report.api.dto.SfcReportDto;
import com.itl.iap.report.api.entity.Sfc;

public interface SfcReportService extends IService<Sfc> {
    IPage<Sfc> queryList(SfcReportDto sfcReportDto);
}
