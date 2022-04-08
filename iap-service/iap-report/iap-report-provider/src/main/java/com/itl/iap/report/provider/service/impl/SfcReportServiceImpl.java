package com.itl.iap.report.provider.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.report.api.dto.SfcReportDto;
import com.itl.iap.report.api.service.SfcReportService;
import com.itl.iap.report.provider.mapper.SfcReportMapper;
import com.itl.iap.report.api.entity.Sfc;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SfcReportServiceImpl extends ServiceImpl<SfcReportMapper, Sfc> implements SfcReportService {

    @Autowired
    private SfcReportMapper reportMapper;


    @Override
    public IPage<Sfc> queryList(SfcReportDto sfcReportDto) {
        String[] operationNames=null;
        if(StringUtils.isNotBlank(sfcReportDto.getOperationNames())){
            operationNames=sfcReportDto.getOperationNames().split(",");
        }
        IPage<Sfc> page=reportMapper.queryList(sfcReportDto.getPage(),sfcReportDto.getShopOrder(),sfcReportDto.getOperationOrder(),operationNames,sfcReportDto.getItem(),sfcReportDto.getSfc(),sfcReportDto.getState());
        return page;
    }
}
