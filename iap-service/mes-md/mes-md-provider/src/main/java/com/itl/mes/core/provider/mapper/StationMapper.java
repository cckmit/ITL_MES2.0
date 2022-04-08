package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.core.api.dto.UserStationQueryDTO;
import com.itl.mes.core.api.entity.Station;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工位表 Mapper 接口
 * </p>
 *
 * @author space
 * @since 2019-05-30
 */

public interface StationMapper extends BaseMapper<Station> {

    Map<String,Object> getByStation(@Param("station")String station,@Param("site")String site);
    IPage<Station> findUserStations(@Param("page") Page page, @Param("userStationQueryDTO") UserStationQueryDTO userStationQueryDTO);

    IPage<Station> findUncorrelatedUserStations(@Param("page") Page page, @Param("userStationQueryDTO") UserStationQueryDTO userStationQueryDTO);
    List<Station> getStationBySiteAndUserName(@Param("site") String site,@Param("userName") String userName);

    List<Station> getStationBySiteAndCardNumber(@Param("site")String site,@Param("userCardNumber") String userNameOrCardNumber);

    List<String> getUserByStationBo(@Param("stationBo") String stationBo);

    String getUserNameByUserBo(@Param("userBo") String userBo);

    Station getStationByStepAndProductLineBo(@Param("stepBo") String stepBo,@Param("productLineBo") String productLineBo);

    String getProductLineBoByUser(@Param("userName") String userName);
}