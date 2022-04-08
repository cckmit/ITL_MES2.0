package com.itl.iap.mes.provider.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.mes.api.entity.DataCollectionType;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @time 2018年11月14日
 * @since JDK 1.8
 */
public interface DataCollectionTypeMapper extends BaseMapper<DataCollectionType> {

    IPage<DataCollectionType> findList(Page page, @Param("dataCollectionType") DataCollectionType dataCollectionType);

    IPage<DataCollectionType> findListByState(Page page, @Param("dataCollectionType") DataCollectionType dataCollectionType);
}
