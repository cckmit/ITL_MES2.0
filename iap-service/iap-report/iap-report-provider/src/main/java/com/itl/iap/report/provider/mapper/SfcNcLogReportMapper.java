package com.itl.iap.report.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.report.api.dto.SfcScrapDto;
import com.itl.iap.report.api.entity.SfcNcLog;
import com.itl.iap.report.api.entity.SfcScrap;
import org.apache.ibatis.annotations.Param;

public interface SfcNcLogReportMapper extends BaseMapper<SfcNcLog> {
    IPage<SfcNcLog> queryList(Page page, @Param("shopOrder") String shopOrder, @Param("operationOrder") String operationOrder, @Param("operationName") String operationName, @Param("state") String state, @Param("item") String item, @Param("sfc") String sfc, @Param("ncName") String ncName, @Param("dutyOperation") String dutyOperation);

    IPage<SfcScrap> querySfcScrap(Page page, @Param("sfcScrapDto") SfcScrapDto sfcScrapDto);

    String selectSyncState(@Param("bo") String bo);

    int updateSyncStatus(@Param("bo") String bo);

    int updateFailReason(@Param("json") String json, @Param("bo") String bo);

    String selectOrderNumber(@Param("bo") String bo);

    int updateOrderNumber(@Param("orderNumber") String orderNumber, @Param("bo") String bo);

    String selectMaxOrderNumber(@Param("ERP_NO_PART") String orderNumber);

    String findTruename(@Param("dutyUser") String dutyUser);

    String catchUsers(@Param("bo") String bo);
}
