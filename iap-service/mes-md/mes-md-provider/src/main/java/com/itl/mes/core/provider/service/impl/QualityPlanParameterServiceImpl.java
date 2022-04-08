package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.mes.core.api.entity.QualityPlanParameter;
import com.itl.mes.core.api.service.QualityPlanParameterService;
import com.itl.mes.core.provider.mapper.QualityPlanParameterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author space
 * @since 2019-08-30
 */
@Service
@Transactional
public class QualityPlanParameterServiceImpl extends ServiceImpl<QualityPlanParameterMapper, QualityPlanParameter> implements QualityPlanParameterService {


    @Autowired
    private QualityPlanParameterMapper qualityPlanParameterMapper;

    @Override
    public List<QualityPlanParameter> selectList() {
        QueryWrapper<QualityPlanParameter> entityWrapper = new QueryWrapper<>();
        //getEntityWrapper(entityWrapper, qualityPlanParameter);
        return super.list(entityWrapper);
    }


}