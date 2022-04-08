package com.itl.iap.mes.provider.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.api.dto.toolgroup.ToolGroupNumberQueryDTO;
import com.itl.iap.mes.api.entity.toolgroup.ToolGroupEntity;
import com.itl.iap.mes.api.entity.toolgroup.ToolNumberEntity;
import com.itl.iap.mes.api.service.ToolGroupNumberService;
import com.itl.iap.mes.api.vo.ToolGroupNumberVo;
import com.itl.iap.mes.provider.mapper.ToolGroupMapper;
import com.itl.iap.mes.provider.mapper.ToolGroupNumberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author liuchenghao
 * @date 2020/11/6 17:17
 */
@Service
public class ToolGroupNumberServiceImpl implements ToolGroupNumberService {

    @Autowired
    ToolGroupNumberMapper toolGroupNumberMapper;


    @Override
    public IPage<ToolGroupNumberVo> findList(ToolGroupNumberQueryDTO toolGroupNumberQueryDTO) {

        if(ObjectUtil.isEmpty(toolGroupNumberQueryDTO.getPage())){
            toolGroupNumberQueryDTO.setPage(new Page(0, 10));
        }

        toolGroupNumberQueryDTO.setSite(UserUtils.getSite());
        return toolGroupNumberMapper.findList(toolGroupNumberQueryDTO.getPage(),toolGroupNumberQueryDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(ToolNumberEntity toolNumberEntity) {

        if(StrUtil.isNotEmpty(toolNumberEntity.getBo())){
            toolNumberEntity.setModifyDate(new Date());
            toolNumberEntity.setModifyUser(UserUtils.getCurrentUser().getUserName());
            toolGroupNumberMapper.updateById(toolNumberEntity);
        }else{
            toolNumberEntity.setSite(UserUtils.getSite());
            toolNumberEntity.setCreateDate(new Date());
            toolNumberEntity.setCreateUser(UserUtils.getCurrentUser().getUserName());
            toolGroupNumberMapper.insert(toolNumberEntity);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<String> ids) {

        ids.forEach(id ->{
            toolGroupNumberMapper.deleteById(id);
        });
    }

    @Autowired
    ToolGroupMapper toolGroupMapper;
    @Override
    public ToolNumberEntity findById(String id){
        ToolNumberEntity toolNumberEntity = toolGroupNumberMapper.selectById(id);
        if(toolNumberEntity!=null){
            String toolGroupBo = toolNumberEntity.getToolGroupBo();
            ToolGroupEntity toolGroupEntity = toolGroupMapper.selectById(toolGroupBo);
            if(toolGroupEntity != null){
                toolNumberEntity.setToolGroup(toolGroupEntity.getToolGroup());
                toolNumberEntity.setToolGroupQty(toolGroupEntity.getQty());
            }
        }
        return toolNumberEntity;
    }



}
