package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.mes.core.api.entity.ListParameter;
import com.itl.mes.core.api.service.ListParameterService;
import com.itl.mes.core.provider.mapper.ListParameterMapper;
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
 * @since 2019-06-03
 */
@Service
@Transactional
public class ListParameterServiceImpl extends ServiceImpl<ListParameterMapper, ListParameter> implements ListParameterService {


    @Autowired
    private ListParameterMapper listParameterMapper;

    @Override
    public List<ListParameter> selectList() {
        QueryWrapper<ListParameter> entityWrapper = new QueryWrapper<ListParameter>();
        //getEntityWrapper(entityWrapper, listParameter);
        return super.list(entityWrapper);
    }


}