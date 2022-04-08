package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.core.api.dto.StockDTO;
import com.itl.mes.core.api.entity.Stock;
import org.apache.ibatis.annotations.Param;

public interface StockMapper extends BaseMapper<Stock> {
    IPage<Stock> getStock(Page page,@Param("stockDTO") StockDTO stockDTO);
}
