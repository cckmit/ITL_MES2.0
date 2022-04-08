package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.mes.core.api.entity.QualityCheckParameter;
import com.itl.mes.core.api.service.QualityCheckParameterService;
import com.itl.mes.core.provider.mapper.QualityCheckParameterMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class QualityCheckParameterServiceImpl extends ServiceImpl<QualityCheckParameterMapper, QualityCheckParameter> implements QualityCheckParameterService {
}
