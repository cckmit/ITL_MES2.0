package com.itl.iap.mes.provider.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.mes.api.dto.demand.DemandQueryDTO;

import com.itl.iap.mes.api.entity.demand.DemandEntity;
import com.itl.iap.mes.api.entity.label.LabelEntity;

import com.itl.iap.mes.provider.mapper.DemandMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author liuchenghao
 * @date 2020/11/5 16:54
 */
@Service
public class DemandServerImpl {



    @Autowired
    DemandMapper demandMapper;


    public IPage<DemandEntity> findList(DemandQueryDTO demandQueryDTO) {

        if(ObjectUtil.isEmpty(demandQueryDTO.getPage())){
            demandQueryDTO.setPage(new Page(0, 10));
        }

        QueryWrapper queryWrapper = new QueryWrapper<DemandEntity>();

        if(StrUtil.isNotEmpty(demandQueryDTO.getDemandNo())){
            queryWrapper.like("demand_no",demandQueryDTO.getDemandNo());
        }
        if(StrUtil.isNotEmpty(demandQueryDTO.getCustomerBo())){
            queryWrapper.eq("customer_bo",demandQueryDTO.getCustomerBo());
        }
        if(StrUtil.isNotEmpty(demandQueryDTO.getDemandType())){
            queryWrapper.eq("demand_type",demandQueryDTO.getDemandType());
        }
        if(StrUtil.isNotEmpty(demandQueryDTO.getMaterialNo())){
            queryWrapper.eq("material_no",demandQueryDTO.getMaterialNo());
        }
        if(ObjectUtil.isNotEmpty(demandQueryDTO.getCreateStartDate())&& ObjectUtil.isNotEmpty(demandQueryDTO.getCreateEndDate())){
            queryWrapper.between("create_date",demandQueryDTO.getCreateStartDate(),demandQueryDTO.getCreateEndDate());
        }
        if(ObjectUtil.isNotEmpty(demandQueryDTO.getPlanStartDate())&& ObjectUtil.isNotEmpty(demandQueryDTO.getPlanEndDate())){
            queryWrapper.between("plan_date",demandQueryDTO.getPlanStartDate(),demandQueryDTO.getPlanEndDate());
        }

        return demandMapper.selectPage(demandQueryDTO.getPage(),queryWrapper);
    }

    @Transactional
    public void save(DemandEntity demandEntity) {

        if(StrUtil.isNotEmpty(demandEntity.getId())){
            demandMapper.updateById(demandEntity);
        }else{
            demandMapper.insert(demandEntity);
        }
    }


    @Transactional
    public void delete(List<String> ids) {

        ids.forEach(id ->{
            DemandEntity demandEntity = new DemandEntity();
            demandEntity.setId(id);
            demandEntity.setIsDelete(1);
            demandMapper.updateById(demandEntity);
        });
    }


    public DemandEntity findById(String id){

        return demandMapper.selectById(id);
    }



}
