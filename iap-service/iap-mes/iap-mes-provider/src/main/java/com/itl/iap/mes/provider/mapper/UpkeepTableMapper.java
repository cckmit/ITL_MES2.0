package com.itl.iap.mes.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.mes.api.entity.UpkeepTable;
import org.apache.ibatis.annotations.Param;

public interface UpkeepTableMapper extends BaseMapper<UpkeepTable> {
    IPage<UpkeepTable> selectRepairTablePage(Page page, @Param("deviceName") String deviceName, @Param("repairRealName") String repairRealName, @Param("ksTime") String ksTime, @Param("jsTime") String jsTime,@Param("deviceCode") String deviceCode);
    String selectMaxCode();
    UpkeepTable selectByRepairId(@Param("repairId") String repairId);
}
