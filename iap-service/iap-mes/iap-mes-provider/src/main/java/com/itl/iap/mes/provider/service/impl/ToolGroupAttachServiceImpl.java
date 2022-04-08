package com.itl.iap.mes.provider.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.api.dto.toolgroup.ToolGroupAttachQueryDTO;
import com.itl.iap.mes.api.dto.toolgroup.ToolGroupAttachResDTO;
import com.itl.iap.mes.api.entity.toolgroup.ToolGroupAttachEntity;
import com.itl.iap.mes.api.service.ToolGroupAttachService;
import com.itl.iap.mes.provider.common.CommonCode;
import com.itl.iap.mes.provider.exception.CustomException;
import com.itl.iap.mes.provider.mapper.ToolGroupAttachMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author liuchenghao
 * @date 2020/11/9 10:16
 */
@Service
public class ToolGroupAttachServiceImpl  implements ToolGroupAttachService {



    @Autowired
    ToolGroupAttachMapper toolGroupAttachMapper;

    @Override
    public IPage<ToolGroupAttachResDTO> findList(ToolGroupAttachQueryDTO toolGroupAttachQueryDTO) {

        if(ObjectUtil.isEmpty(toolGroupAttachQueryDTO.getPage())){
            toolGroupAttachQueryDTO.setPage(new Page(0, 10));
        }
//        toolGroupAttachQueryDTO.setSite(UserUtils.getSite());
        toolGroupAttachQueryDTO.setSite("1040");
        return toolGroupAttachMapper.findList(toolGroupAttachQueryDTO.getPage(),toolGroupAttachQueryDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(ToolGroupAttachEntity toolGroupAttachEntity) {

        if(StrUtil.isNotEmpty(toolGroupAttachEntity.getBo())){
            toolGroupAttachMapper.updateById(toolGroupAttachEntity);
        }else{
            QueryWrapper<ToolGroupAttachEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("context_bo",toolGroupAttachEntity.getContextBo());
            queryWrapper.eq("type",toolGroupAttachEntity.getType());
            queryWrapper.eq("tool_group_bo",toolGroupAttachEntity.getToolGroupBo());
            Integer count = toolGroupAttachMapper.selectCount(queryWrapper);
            if(count>0){
                throw new CustomException(CommonCode.DATA_REPEAT);
            }
            toolGroupAttachEntity.setSite(UserUtils.getSite());
            toolGroupAttachMapper.insert(toolGroupAttachEntity);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<String> ids) {

        ids.forEach(id ->{
            toolGroupAttachMapper.deleteById(id);
        });
    }

    @Override
    public ToolGroupAttachResDTO findById(String id){

        return toolGroupAttachMapper.findById(id);
    }




}
