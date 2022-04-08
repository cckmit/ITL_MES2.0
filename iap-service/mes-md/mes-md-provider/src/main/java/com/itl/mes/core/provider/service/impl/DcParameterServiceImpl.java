package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.DcParameterHandleBO;
import com.itl.mes.core.api.entity.DcParameter;
import com.itl.mes.core.api.service.DcParameterService;
import com.itl.mes.core.provider.mapper.DcParameterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 数据收集组参数表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-06-04
 */
@Service
@Transactional
public class DcParameterServiceImpl extends ServiceImpl<DcParameterMapper, DcParameter> implements DcParameterService {


    @Autowired
    private DcParameterMapper dcParameterMapper;

    @Override
    public List<DcParameter> selectList() {
        QueryWrapper<DcParameter> entityWrapper = new QueryWrapper<>();
        //getEntityWrapper(entityWrapper, dcParameter);
        return super.list(entityWrapper);
    }

    @Override
    public DcParameter getDcParameterByParamName(String paramName,String dcGroup) throws CommonException {
        DcParameterHandleBO parameterHandleBO = new DcParameterHandleBO(UserUtils.getSite(), dcGroup, paramName);
        DcParameter dcParameter = dcParameterMapper.selectById(parameterHandleBO);
        if (dcParameter == null) {
            throw new CommonException("当前收集组下参数名" + paramName + "未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return dcParameter;
    }

    @Override
    public List<DcParameter> getDcParameterVoByFuzzy(String paramName) throws CommonException {
        QueryWrapper<DcParameter> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(DcParameter.SITE, UserUtils.getSite());
        entityWrapper.like(DcParameter.PARAM_NAME, paramName);
        List<DcParameter> dcParameters = super.list(entityWrapper);
        if (dcParameters == null){
            throw new CommonException("参数名" + paramName + "未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }

        return dcParameters;
    }


}