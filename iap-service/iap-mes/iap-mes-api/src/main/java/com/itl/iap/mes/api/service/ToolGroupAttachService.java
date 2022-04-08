package com.itl.iap.mes.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.mes.api.dto.toolgroup.ToolGroupAttachQueryDTO;
import com.itl.iap.mes.api.dto.toolgroup.ToolGroupAttachResDTO;
import com.itl.iap.mes.api.entity.toolgroup.ToolGroupAttachEntity;

import java.util.List;

/**
 * @auth liuchenghao
 * @date 2021/1/15
 */
public interface ToolGroupAttachService {

    IPage<ToolGroupAttachResDTO> findList(ToolGroupAttachQueryDTO toolGroupAttachQueryDTO);


    void save(ToolGroupAttachEntity toolGroupAttachEntity);


    void delete(List<String> ids);


    ToolGroupAttachResDTO findById(String id);
}
