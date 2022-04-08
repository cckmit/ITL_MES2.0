package com.itl.iap.system.provider.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author 崔翀赫
 * @date 2021/1/28$
 * @since JDK1.8
 */
public interface StationMapper {
    /**
     * 根据工位查产线车间
     * @param station
     * @param site
     * @return
     */
    Map<String,Object> getByStation(@Param("station")String station, @Param("site")String site);

    String getProductLineBoByStation(@Param("stationBo") String stationBo);
}
