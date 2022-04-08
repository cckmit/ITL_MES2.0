package com.itl.iap.mes.provider.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.mes.api.entity.DataCollectionItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 
 * @time 2018年11月14日
 * @since JDK 1.8
 */
@Mapper
public interface DataCollectionItemMapper extends BaseMapper<DataCollectionItem> {

    IPage<DataCollectionItem> pageQuery(Page page, @Param("dataCollectionId") String dataCollectionId);
}
