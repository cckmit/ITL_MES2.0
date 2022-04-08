package com.itl.iap.mes.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.mes.api.dto.toolgroup.ToolGroupNumberQueryDTO;
import com.itl.iap.mes.api.entity.toolgroup.ToolNumberEntity;
import com.itl.iap.mes.api.vo.ToolGroupNumberVo;
import org.apache.ibatis.annotations.Param;

/**
 * @author liuchenghao
 * @date 2020/11/6 17:13
 */
public interface ToolGroupNumberMapper extends BaseMapper<ToolNumberEntity> {


    IPage<ToolGroupNumberVo> findList(Page page, @Param("toolGroupNumberQueryDTO") ToolGroupNumberQueryDTO toolGroupNumberQueryDTO);

}
