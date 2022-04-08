package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.core.api.entity.StationType;
import com.itl.mes.core.api.vo.StationTypeItemVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 工位类型表 Mapper 接口
 * </p>
 *
 * @author space
 * @since 2019-05-31
 */

public interface StationTypeMapper extends BaseMapper<StationType> {

    List<StationTypeItemVo> getAvailableStationTypeItemVos(@Param("site") String site, @Param("station") String station, @Param("stationTypeBO") String stationTypeBO);

    List<StationTypeItemVo> getAssignedStationTypeItemVos(@Param("site") String site, @Param("stationTypeBO") String stationTypeBO);

}