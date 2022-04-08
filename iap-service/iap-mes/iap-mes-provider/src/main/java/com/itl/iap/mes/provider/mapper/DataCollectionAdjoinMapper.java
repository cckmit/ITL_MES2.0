package com.itl.iap.mes.provider.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.mes.api.dto.DataCollectionVo;
import com.itl.iap.mes.api.entity.DataCollection;
import com.itl.iap.mes.api.entity.DataCollectionAdjoin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 
 * @time 2018年11月14日
 * @since JDK 1.8
 */
@Mapper
public interface DataCollectionAdjoinMapper extends BaseMapper<DataCollectionAdjoin> {

    IPage<DataCollectionAdjoin> findList(Page page, @Param("dataCollectionId") String dataCollectionId);

}
