package com.itl.mes.core.provider.schedule;

import com.itl.mes.core.provider.mapper.MyDeviceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * mes定时任务类
 */
@Slf4j
@Component
@EnableScheduling
public class MesSchedule {

    @Autowired
    private MyDeviceMapper myDeviceMapper;

    /**
     * 定时更新首检状态（每天凌晨6点）
     */
    @Scheduled(cron = "0 0 6 * * ? ")
    public void firstInsStateSchedule(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info("===================" + simpleDateFormat.format(new Date()) + "开始更新所有的首检状态" + "===================");

        //=========================开始更新=========================
        try {
            myDeviceMapper.updateAllFirstInsState();
        } catch (Exception e) {
            log.error("首检更新异常===================" + e.toString());
            e.printStackTrace();
        }
        //=========================结束更新=========================

        log.info("===================" + simpleDateFormat.format(new Date()) + "首检状态全部更新已完成" + "===================");
    }
}
