package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.mes.core.api.entity.Attached;
import com.itl.mes.core.api.service.AttachedService;
import com.itl.mes.core.provider.mapper.AttachedMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lzh
 * @since 2019-08-30
 */
@Service
@Transactional
public class AttachedServiceImpl extends ServiceImpl<AttachedMapper, Attached> implements AttachedService {


    @Autowired
    private AttachedMapper attachedMapper;

    @Override
    public List<Attached> selectList() {
        QueryWrapper<Attached> entityWrapper = new QueryWrapper<Attached>();
        //getEntityWrapper(entityWrapper, attached);
        return super.list(entityWrapper);
    }


}