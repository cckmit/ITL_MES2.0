package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.core.api.dto.ReportWorkDto;
import com.itl.mes.core.api.dto.ReportWorkListDto;
import com.itl.mes.core.api.entity.ReportWork;
import com.itl.mes.core.api.entity.WorkStation;
import com.itl.mes.core.api.vo.ReportWorkVo;
import com.itl.mes.core.api.vo.StepScrapVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ReportWorkMapper extends BaseMapper<ReportWork> {

    IPage<ReportWorkVo> selectCanReportWorkByStep(Page page,@Param("reportWorkDto") ReportWorkDto reportWorkDto);

    List<ReportWork> getCanWorkQtyByStep(@Param("reportWorkDto")ReportWorkDto reportWorkDto);

    IPage<ReportWork> selectReportWorkInfo(Page page,@Param("reportWorkListDto")ReportWorkListDto reportWorkListDto);

    List<ReportWork> selectReportWorkInfo(@Param("reportWorkListDto")ReportWorkListDto reportWorkListDto);

    List<WorkStation> getStationByUser(@Param("userId") String userId);

    IPage<ReportWork> selectReportWorkRecord(Page page,@Param("reportWorkDto")ReportWorkDto reportWorkDto);

    StepScrapVo getStepScrapBySfc(@Param("sfc") String sfc);

    String getStepScrapQtyByWorkStep(@Param("workStepBo") String workStepBo,@Param("sfc") String sfc);

    BigDecimal getCanReportTotalQty(@Param("operationBo") String operationBo,@Param("sfc") String sfc);

    BigDecimal getCanReportQtyByStep(@Param("operationBo") String operationBo,@Param("sfc") String sfc,@Param("workStepCodeBo") String workStepCodeBo);
}
