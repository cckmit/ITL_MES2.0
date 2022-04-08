package com.itl.iap.report.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.report.api.entity.Sfc;
import org.apache.ibatis.annotations.Param;

public interface SfcReportMapper extends BaseMapper<Sfc> {
    IPage<Sfc> queryList(Page page, @Param("shopOrder") String shopOrder,@Param("operationOrder") String operationOrder,@Param("operationNames") String[] operationNames,@Param("item") String item,@Param("sfc") String sfc,@Param("state") String state);
}
