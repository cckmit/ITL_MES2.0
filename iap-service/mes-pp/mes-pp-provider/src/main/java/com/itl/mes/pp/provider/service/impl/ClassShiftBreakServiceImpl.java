package com.itl.mes.pp.provider.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.pp.api.dto.scheduleplan.ClassShiftBreakQueryDTO;
import com.itl.mes.pp.api.entity.scheduleplan.ClassShiftBreakEntity;
import com.itl.mes.pp.api.service.ClassShiftBreakService;
import com.itl.mes.pp.provider.mapper.ClassShiftBreakMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassShiftBreakServiceImpl implements ClassShiftBreakService {


    @Autowired
    ClassShiftBreakMapper classShiftBreakMapper;

    @Override
    public IPage<ClassShiftBreakEntity> findList(ClassShiftBreakQueryDTO classShiftBreakQueryDTO) {

        if(ObjectUtil.isEmpty(classShiftBreakQueryDTO.getPage())){
            classShiftBreakQueryDTO.setPage(new Page(0, 10));
        }

        QueryWrapper<ClassShiftBreakEntity> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("class_frequency_id",classShiftBreakQueryDTO.getClassFrequencyId());

        return classShiftBreakMapper.selectPage(classShiftBreakQueryDTO.getPage(),queryWrapper);
    }

    @Override
    public void save(ClassShiftBreakEntity classShiftBreakEntity) {


        if(StrUtil.isNotBlank(classShiftBreakEntity.getId())){

            classShiftBreakMapper.updateById(classShiftBreakEntity);
        }else {
            classShiftBreakMapper.insert(classShiftBreakEntity);
        }

    }

    @Override
    public void delete(List<String> ids) {

        ids.forEach(id -> {
            classShiftBreakMapper.deleteById(id);
        });
    }

    @Override
    public ClassShiftBreakEntity findById(String id) {

        return classShiftBreakMapper.selectById(id);

    }
}
