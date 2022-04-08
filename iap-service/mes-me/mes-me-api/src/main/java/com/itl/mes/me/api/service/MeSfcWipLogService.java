package com.itl.mes.me.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.me.api.dto.snLifeCycle.StationRecord;
import com.itl.mes.me.api.entity.MeSfc;
import com.itl.mes.me.api.entity.MeSfcWipLog;

import java.util.Date;
import java.util.List;


/**
 * 生产过程日志
 *
 * @author renren
 * @date 2021-01-25 14:43:26
 */
public interface MeSfcWipLogService extends IService<MeSfcWipLog> {

    /**
     * 过站记录
     *
     * @param sn
     * @return
     */
    List<StationRecord> getStationRecord(String sn);


    /**
     * 同用日志记录
     * @param meSfc
     * @param outTime   出站时间
     * @param result    OK / NG
     * @param isRework    是否是重复加工
     * @return
     */
    MeSfcWipLog recordLog(MeSfc meSfc, Date outTime, String result, String isRework);
}

