package com.itl.mes.me.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.me.api.dto.SnLogDto;
import com.itl.mes.me.api.entity.SnLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * SN日志表
 *
 * @author cuichonghe
 * @date 2020-12-25 12:36:32
 */
@Mapper
public interface SnLogMapper extends BaseMapper<SnLog> {


    /**
     * getAll
     * @param page
     * @param snLogDto
     * @return
     */
    IPage<SnLog> getAll(Page page, @Param("snLogDto") SnLogDto snLogDto);

}
