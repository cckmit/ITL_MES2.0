package com.itl.iap.mes.provider.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.mes.api.dto.label.LabelTypeQueryDTO;
import com.itl.iap.mes.api.entity.label.LabelTypeEntity;
import com.itl.iap.mes.provider.common.CommonCode;
import com.itl.iap.mes.provider.exception.CustomException;
import com.itl.iap.mes.provider.mapper.LabelTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author liuchenghao
 * @date 2020/11/3 17:59
 */
@Service
public class LabelTypeServiceImpl {


    @Autowired
    LabelTypeMapper labelTypeMapper;


    public IPage<LabelTypeEntity> findList(LabelTypeQueryDTO labelTypeQueryDTO) {

        if(ObjectUtil.isEmpty(labelTypeQueryDTO.getPage())){
            labelTypeQueryDTO.setPage(new Page(0, 10));
        }

        QueryWrapper queryWrapper = new QueryWrapper<LabelTypeEntity>();
        if(StrUtil.isNotEmpty(labelTypeQueryDTO.getSite())){
            queryWrapper .eq("site",labelTypeQueryDTO.getSite());
        }

        if(StrUtil.isNotEmpty(labelTypeQueryDTO.getLabelType())){
            queryWrapper.like("label_type",labelTypeQueryDTO.getLabelType());
        }
        labelTypeQueryDTO.getPage().setDesc("CREATION_DATE");
        return labelTypeMapper.selectPage(labelTypeQueryDTO.getPage(),queryWrapper);
    }

    public IPage<LabelTypeEntity> findListByState(LabelTypeQueryDTO labelTypeQueryDTO) {

        if(ObjectUtil.isEmpty(labelTypeQueryDTO.getPage())){
            labelTypeQueryDTO.setPage(new Page(0, 10));
        }

        QueryWrapper queryWrapper = new QueryWrapper<LabelTypeEntity>();
        if(StrUtil.isNotEmpty(labelTypeQueryDTO.getSite())){
            queryWrapper .eq("site",labelTypeQueryDTO.getSite());
        }

        if(StrUtil.isNotEmpty(labelTypeQueryDTO.getLabelType())){
            queryWrapper.like("label_type",labelTypeQueryDTO.getLabelType());
        }
        queryWrapper.eq("STATE", 1);
        labelTypeQueryDTO.getPage().setDesc("CREATION_DATE");
        return labelTypeMapper.selectPage(labelTypeQueryDTO.getPage(),queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(LabelTypeEntity labelTypeEntity) throws CommonException {
        LambdaQueryWrapper<LabelTypeEntity> query = new QueryWrapper<LabelTypeEntity>().lambda().eq(LabelTypeEntity::getLabelType, labelTypeEntity.getLabelType());
        if (ObjectUtil.isNotEmpty(labelTypeEntity.getId())) {
            query.ne(LabelTypeEntity::getId,labelTypeEntity.getId());
        }
        List<LabelTypeEntity> list = labelTypeMapper.selectList(query);
        if (list != null && list.size() > 0) {
                throw new CustomException(CommonCode.CODE_REPEAT);
        }

        if(ObjectUtil.isNotEmpty(labelTypeEntity.getId())){
            labelTypeEntity.setLastUpdateDate(new Date());
            labelTypeMapper.updateById(labelTypeEntity);
        }else{
            labelTypeEntity.setCreationDate(new Date());
            labelTypeMapper.insert(labelTypeEntity);
        }
    }


    @Transactional
    public void delete(List<String> ids) {
        ids.forEach(id ->{
            labelTypeMapper.deleteById(id);
        });
    }


    public LabelTypeEntity findById(String id){

        return labelTypeMapper.selectById(id);
    }



}
