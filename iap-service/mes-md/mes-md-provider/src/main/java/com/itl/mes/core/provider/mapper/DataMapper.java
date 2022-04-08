package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.core.api.entity.WorkShop;
import org.apache.ibatis.annotations.Param;

public interface DataMapper extends BaseMapper<WorkShop> {
    WorkShop getWorkShopBoByStationBo(@Param("stationBo") String stationBo);
}
