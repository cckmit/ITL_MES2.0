package com.itl.iap.mes.provider.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.api.dto.toolgroup.ToolGroupQueryDTO;
import com.itl.iap.mes.api.entity.toolgroup.ToolGroupEntity;
import com.itl.iap.mes.provider.common.CommonCode;
import com.itl.iap.mes.provider.exception.CustomException;
import com.itl.iap.mes.provider.mapper.ToolGroupMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author liuchenghao
 * @date 2020/11/6 15:55
 */
@Service
public class ToolGroupServiceImpl {



    @Autowired
    ToolGroupMapper toolGroupMapper;


    public IPage<ToolGroupEntity> findList(ToolGroupQueryDTO toolGroupQueryDTO) {

        if(ObjectUtil.isEmpty(toolGroupQueryDTO.getPage())){
            toolGroupQueryDTO.setPage(new Page(0, 10));
        }
        toolGroupQueryDTO.getPage().setDesc("CREATE_DATE");
        QueryWrapper queryWrapper = new QueryWrapper<ToolGroupEntity>();

        if(StrUtil.isNotEmpty(toolGroupQueryDTO.getToolGroup())){
            queryWrapper.like("tool_group",toolGroupQueryDTO.getToolGroup());
        }
        if(StrUtil.isNotEmpty(toolGroupQueryDTO.getState())){
            queryWrapper.eq("state",toolGroupQueryDTO.getState());
        }
        if(StrUtil.isNotEmpty(toolGroupQueryDTO.getTgDesc())){
            queryWrapper.like("tg_desc",toolGroupQueryDTO.getTgDesc());
        }
        if(StrUtil.isNotEmpty(toolGroupQueryDTO.getSite())){
            queryWrapper.eq("site",toolGroupQueryDTO.getSite());
        }
        return toolGroupMapper.selectPage(toolGroupQueryDTO.getPage(),queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    public void save(ToolGroupEntity toolGroupEntity) {

        if(StrUtil.isNotEmpty(toolGroupEntity.getBo())){
            toolGroupEntity.setModifyDate(new Date());
            toolGroupEntity.setModifyUser(UserUtils.getCurrentUser().getUserName());
            toolGroupMapper.updateById(toolGroupEntity);
        }else{
            QueryWrapper<ToolGroupEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tool_group",toolGroupEntity.getToolGroup());
            Integer count = toolGroupMapper.selectCount(queryWrapper);
            if(count>0){
                throw new CustomException(CommonCode.CODE_REPEAT);
            }
            toolGroupEntity.setSite(UserUtils.getSite());
            toolGroupEntity.setCreateDate(new Date());
            toolGroupEntity.setCreateUser(UserUtils.getCurrentUser().getUserName());
            toolGroupMapper.insert(toolGroupEntity);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void delete(List<String> ids) {

        ids.forEach(id ->{
           toolGroupMapper.deleteById(id);
        });
    }


    public ToolGroupEntity findById(String id){

        return toolGroupMapper.selectById(id);
    }




}
