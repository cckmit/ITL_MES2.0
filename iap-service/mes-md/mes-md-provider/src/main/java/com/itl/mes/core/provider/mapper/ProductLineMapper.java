package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.core.api.entity.ProductLine;
import com.itl.mes.core.api.vo.ProductLineQueryVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 产线表 Mapper 接口
 * </p>
 *
 * @author space
 * @since 2019-05-24
 */

public interface ProductLineMapper extends BaseMapper<ProductLine> {
    /**
     * 根据车间查询
     * @param page
     * @param queryVo
     * @return
     */
    List<ProductLine> listByWorkShop(Page page, @Param("queryVo") ProductLineQueryVo queryVo);
}
