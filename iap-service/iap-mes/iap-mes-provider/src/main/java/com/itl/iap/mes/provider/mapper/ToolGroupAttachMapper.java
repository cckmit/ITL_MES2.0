package com.itl.iap.mes.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.mes.api.dto.toolgroup.ToolGroupAttachQueryDTO;
import com.itl.iap.mes.api.dto.toolgroup.ToolGroupAttachResDTO;
import com.itl.iap.mes.api.entity.toolgroup.ToolGroupAttachEntity;
import org.apache.ibatis.annotations.Param;

/**
 * @author liuchenghao
 * @date 2020/11/9 10:15
 */
public interface ToolGroupAttachMapper extends BaseMapper<ToolGroupAttachEntity> {


    IPage<ToolGroupAttachResDTO> findList(@Param("page") Page page, @Param("toolGroupAttachQueryDTO") ToolGroupAttachQueryDTO toolGroupAttachQueryDTO);


    ToolGroupAttachResDTO findById(String id);
}
