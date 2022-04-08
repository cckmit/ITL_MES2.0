package com.itl.mes.me.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.me.api.dto.SnDto;
import com.itl.mes.me.api.entity.Sn;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * 条码信息表
 *
 * @author cuichonghe
 * @date 2020-12-25 12:36:32
 */
@Repository
public interface SnMapper extends BaseMapper<Sn> {
    /**
     * getAll
     * @param page
     * @param snDto
     * @return
     */
    IPage<Sn> getAll(Page page, @Param("snDto") SnDto snDto);

    /**
     * getItemBySn
     * @param sn
     * @return
     */
    Map getItem(@Param("sn") String sn);
}
