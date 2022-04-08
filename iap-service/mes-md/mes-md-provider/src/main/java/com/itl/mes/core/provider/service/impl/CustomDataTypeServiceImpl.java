package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.mes.core.api.entity.CustomDataType;
import com.itl.mes.core.api.service.CustomDataTypeService;
import com.itl.mes.core.provider.mapper.CustomDataTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 自定义数据类型 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-05-29
 */
@Service
@Transactional
public class CustomDataTypeServiceImpl extends ServiceImpl<CustomDataTypeMapper, CustomDataType> implements CustomDataTypeService {


    @Autowired
    private CustomDataTypeMapper customDataTypeMapper;

    @Override
    public List<CustomDataType> selectList() {
        QueryWrapper<CustomDataType> entityWrapper = new QueryWrapper<CustomDataType>();
        //getEntityWrapper(entityWrapper, customDataType);
        return super.list(entityWrapper);
    }


}