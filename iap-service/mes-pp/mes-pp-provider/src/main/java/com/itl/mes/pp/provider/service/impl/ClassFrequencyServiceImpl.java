package com.itl.mes.pp.provider.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.pp.api.dto.scheduleplan.ClassFrequencyQueryDTO;
import com.itl.mes.pp.api.entity.scheduleplan.ClassFrequencyEntity;
import com.itl.mes.pp.api.service.ClassFrequencyService;
import com.itl.mes.pp.provider.mapper.ClassFrequencyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassFrequencyServiceImpl implements ClassFrequencyService {

    @Autowired
    ClassFrequencyMapper classFrequencyMapper;


    @Override
    public IPage<ClassFrequencyEntity> findList(ClassFrequencyQueryDTO classFrequencyQueryDTO) {

        if(ObjectUtil.isEmpty(classFrequencyQueryDTO.getPage())){
            classFrequencyQueryDTO.setPage(new Page(0, 10));
        }
        QueryWrapper<ClassFrequencyEntity> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("class_system_id",classFrequencyQueryDTO.getClassSystemId());

        return classFrequencyMapper.selectPage(classFrequencyQueryDTO.getPage(),queryWrapper);
    }

    @Override
    public void save(ClassFrequencyEntity classFrequencyEntity) {

        if(StrUtil.isNotBlank(classFrequencyEntity.getId())){
            classFrequencyMapper.updateById(classFrequencyEntity);
        }else {
            classFrequencyMapper.insert(classFrequencyEntity);
        }
    }

    @Override
    public void delete(List<String> ids) {

        ids.forEach(id -> {
            classFrequencyMapper.deleteById(id);
        });

    }

    @Override
    public ClassFrequencyEntity findById(String id) {

        return classFrequencyMapper.selectById(id);
    }
}
