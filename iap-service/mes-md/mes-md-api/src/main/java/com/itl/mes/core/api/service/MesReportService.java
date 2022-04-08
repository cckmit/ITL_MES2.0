package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.mes.core.api.dto.OpThroughRateDTO;
import com.itl.mes.core.api.vo.OpThroughRateVo;

public interface MesReportService{
    IPage<OpThroughRateVo> getOperationThroughRateReport(OpThroughRateDTO opThroughRateDTO);
}
