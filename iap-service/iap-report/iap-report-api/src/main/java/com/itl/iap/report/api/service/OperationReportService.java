package com.itl.iap.report.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.report.api.vo.OperationTestQualifiedRateVo;

public interface OperationReportService {
    IPage<OperationTestQualifiedRateVo> getPageListOperationTestQualifiedRate(OperationTestQualifiedRateVo operationTestQualifiedRateVo);
}
