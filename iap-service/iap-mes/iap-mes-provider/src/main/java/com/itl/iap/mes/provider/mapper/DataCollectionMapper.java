package com.itl.iap.mes.provider.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.mes.api.dto.DataCollectionVo;
import com.itl.iap.mes.api.entity.DataCollection;
import com.itl.iap.mes.api.entity.DataCollectionType;
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
public interface DataCollectionMapper extends BaseMapper<DataCollection> {

    IPage<DataCollectionVo> findList(Page page, @Param("params") Map<String,Object> params);

    IPage<DataCollectionVo> findListByState(Page page, @Param("params") Map<String,Object> params);

    DataCollectionVo getById(String id);
}
