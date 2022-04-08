package com.itl.mes.pp.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.mes.pp.api.dto.schedule.ReceiveRespDTO;
import com.itl.mes.pp.api.dto.schedule.ScheduleReceiveDTO;
import com.itl.mes.pp.api.dto.schedule.ScheduleReceiveQueryDTO;
import com.itl.mes.pp.api.dto.schedule.ScheduleReceiveRespDTO;


/**
 * @author liuchenghao
 * @date 2020/11/16 14:52
 */
public interface ScheduleReceiveService {


    /**
     * 查询待接收的排程信息
     * @param scheduleReceiveQueryDTO
     * @return
     */
    IPage<ReceiveRespDTO> receiveList(ScheduleReceiveQueryDTO scheduleReceiveQueryDTO);

    /**
     * 派工接收保存
     * @param scheduleReceiveDTO
     */
    void dispatch(ScheduleReceiveDTO scheduleReceiveDTO) throws Exception;


    /**
     * 查询派工接收的完成数据和历史数据
     * @param scheduleReceiveQueryDTO
     * @return
     */
    IPage<ScheduleReceiveRespDTO> list(ScheduleReceiveQueryDTO scheduleReceiveQueryDTO);

}
