package com.itl.mes.pp.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.pp.api.dto.schedule.ReceiveRespDTO;
import com.itl.mes.pp.api.dto.schedule.ScheduleReceiveQueryDTO;
import com.itl.mes.pp.api.dto.schedule.ScheduleReceiveRespDTO;
import com.itl.mes.pp.api.entity.schedule.ScheduleReceiveEntity;
import org.apache.ibatis.annotations.Param;

/**
 * @author liuchenghao
 * @date 2020/11/16 14:12
 */
public interface ScheduleReceiveMapper extends BaseMapper<ScheduleReceiveEntity> {


    IPage<ReceiveRespDTO> receiveList(Page page, @Param("scheduleReceive") ScheduleReceiveQueryDTO scheduleReceiveQueryDTO);


    IPage<ScheduleReceiveRespDTO> scheduleReceiveList(Page page, @Param("scheduleReceive") ScheduleReceiveQueryDTO scheduleReceiveQueryDTO);
}
