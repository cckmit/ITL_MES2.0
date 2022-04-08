package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.core.api.dto.MouldDto;
import com.itl.mes.core.api.entity.Mould;
import org.apache.ibatis.annotations.Param;

public interface MouldMapper extends BaseMapper<Mould> {
    Mould selectByBo(String bo);

    IPage<Mould> queryPage(Page page, @Param("mouldDto") MouldDto mouldDto);
}
