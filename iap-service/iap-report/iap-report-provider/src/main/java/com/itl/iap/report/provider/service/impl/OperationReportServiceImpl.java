package com.itl.iap.report.provider.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.util.CodeUtils;
import com.itl.iap.report.api.service.OperationReportService;
import com.itl.iap.report.api.vo.OperationTestQualifiedRateVo;
import com.itl.iap.report.provider.mapper.OperationReportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OperationReportServiceImpl implements OperationReportService {

    @Autowired
    private OperationReportMapper operationReportMapper;

    @Override
    public IPage<OperationTestQualifiedRateVo> getPageListOperationTestQualifiedRate(OperationTestQualifiedRateVo operationTestQualifiedRateVo) {
        IPage<OperationTestQualifiedRateVo> pageListOperationTestQualifiedRate = operationReportMapper.getPageListOperationTestQualifiedRate(operationTestQualifiedRateVo.getPage(), operationTestQualifiedRateVo);
        for (OperationTestQualifiedRateVo record : pageListOperationTestQualifiedRate.getRecords()) {
            record.setQty(operationReportMapper.sumDoneQtyByOperationBoAndSfc(record.getOperationBo(), record.getSfc()));
            record.setUnQualified(new BigDecimal(record.getUnQualified()).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
            record.setQualified(new BigDecimal(record.getQualified()).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
            List<String> userNames = operationReportMapper.getOutStationUserByOperationBoAndSfc(record.getOperationBo(), record.getSfc());
            if (CollectionUtil.isNotEmpty(userNames)){
                record.setTaskUser(CodeUtils.formatAndPrint(String::join, ",",userNames));
            }
        }
        return pageListOperationTestQualifiedRate;
    }
}
