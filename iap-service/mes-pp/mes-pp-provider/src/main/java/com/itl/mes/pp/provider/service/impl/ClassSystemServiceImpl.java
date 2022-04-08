package com.itl.mes.pp.provider.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.pp.api.dto.scheduleplan.ClassSystemQueryDTO;
import com.itl.mes.pp.api.entity.scheduleplan.ClassSystemEntity;
import com.itl.mes.pp.api.service.ClassSystemService;
import com.itl.mes.pp.provider.mapper.ClassSystemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassSystemServiceImpl implements ClassSystemService {


    @Autowired
    ClassSystemMapper classSystemMapper;

    @Override
    public IPage<ClassSystemEntity> findList(ClassSystemQueryDTO classSystemQueryDTO) {

        if(ObjectUtil.isEmpty(classSystemQueryDTO.getPage())){
            classSystemQueryDTO.setPage(new Page(0, 10));
        }

        QueryWrapper<ClassSystemEntity> queryWrapper = new QueryWrapper<>();
        if(StrUtil.isNotBlank(classSystemQueryDTO.getCode())){
            queryWrapper.like("code",classSystemQueryDTO.getCode());
        }
        if(StrUtil.isNotBlank(classSystemQueryDTO.getName())){
            queryWrapper.like("name",classSystemQueryDTO.getName());
        }

        return classSystemMapper.selectPage(classSystemQueryDTO.getPage(),queryWrapper);
    }

    @Override
    public void save(ClassSystemEntity classSystemEntity) {

        if(StrUtil.isNotBlank(classSystemEntity.getId())){

            classSystemMapper.updateById(classSystemEntity);

        }else {

            classSystemMapper.insert(classSystemEntity);
        }

    }

    @Override
    public void delete(List<String> ids) {


        ids.forEach(id -> {
            classSystemMapper.deleteById(id);
        });

    }

    @Override
    public ClassSystemEntity findById(String id) {

        return classSystemMapper.selectById(id);
    }
}
