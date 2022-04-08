package com.itl.mes.me.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.me.api.entity.MeSfcAssy;
import com.itl.mes.pp.api.entity.schedule.ScheduleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Sfc装配表
 *
 * @author renren
 * @date 2021-01-25 14:43:36
 */
@Mapper
public interface MeSfcAssyMapper extends BaseMapper<MeSfcAssy> {
    /**
     * 根据排程号获取排程bo
     * @param scheduleNo
     * @return
     */
    ScheduleEntity getSchedule(@Param("scheduleNo") String scheduleNo);

    /**
     * 根据stationBo获取operationBo
     * @param stationBo
     * @return
     */
    String getOperationBoByStationBo(@Param("stationBo") String stationBo);

    /**
     * 根据userBo获取teamBo
     * @param userBo
     * @return
     */
    String getTeamBoByUserBo(@Param("userBo") String userBo);

    /**
     * 根据stationBo,prductLineBo获取deviceBo集合
     * @param stationBo
     * @return
     */
    List<String> getDeviceBo(@Param("stationBo") String stationBo,@Param("productLineBo") String productLineBo);
}
