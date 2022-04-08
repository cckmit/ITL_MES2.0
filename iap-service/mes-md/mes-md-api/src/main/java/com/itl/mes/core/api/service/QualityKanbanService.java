package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.mes.core.api.dto.SfcDataStatisticsDto;
import com.itl.mes.core.api.vo.SfcDataStatisticsVo;


public interface QualityKanbanService {
    IPage<SfcDataStatisticsVo> getSfcDataStatistics(SfcDataStatisticsDto sfcDataStatisticsDto);
}
