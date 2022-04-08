package com.itl.iap.mes.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.mes.api.dto.toolgroup.ToolGroupNumberQueryDTO;
import com.itl.iap.mes.api.entity.toolgroup.ToolNumberEntity;
import com.itl.iap.mes.api.vo.ToolGroupNumberVo;

import java.util.List;

/**
 * @auth liuchenghao
 * @date 2020/12/22
 */
public interface ToolGroupNumberService {

    IPage<ToolGroupNumberVo> findList(ToolGroupNumberQueryDTO toolGroupNumberQueryDTO);

    void save(ToolNumberEntity toolNumberEntity);

    void delete(List<String> ids);


    ToolNumberEntity findById(String id);

}
