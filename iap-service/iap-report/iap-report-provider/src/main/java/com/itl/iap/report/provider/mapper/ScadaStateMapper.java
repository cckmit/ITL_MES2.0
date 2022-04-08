package com.itl.iap.report.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.core.api.dto.ScadaStateDTO;
import com.itl.mes.core.api.entity.ScadaStateEntity;

import java.util.List;

public interface ScadaStateMapper extends BaseMapper<ScadaStateEntity> {

    List<ScadaStateDTO> selectScadaList();
}
