package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.dto.OkReportWorkDto;
import com.itl.mes.core.api.dto.ReportWorkDto;
import com.itl.mes.core.api.dto.ReportWorkListDto;
import com.itl.mes.core.api.entity.ReportWork;
import com.itl.mes.core.api.vo.ReportWorkVo;
import com.itl.mes.core.api.vo.StepScrapVo;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface ReportWorkService extends IService<ReportWork> {

    IPage<ReportWorkVo> getCanReportWorkData(ReportWorkDto reportWorkDto);
    IPage<ReportWork> selectReportWorkInfo(ReportWorkListDto reportWorkListDto) throws CommonException, ParseException;

    void exportReportWork(ReportWorkListDto reportWorkListDto) throws CommonException, ParseException;

    boolean isConfig(OkReportWorkDto okReportWorkDto);

    void scrapRegister(List<StepScrapVo> stepScrapVo) throws CommonException;
}
