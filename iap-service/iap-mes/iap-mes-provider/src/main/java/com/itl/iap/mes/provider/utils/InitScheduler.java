package com.itl.iap.mes.provider.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.mes.api.entity.Fault;
import com.itl.iap.mes.api.entity.ScheduleJobEntity;
import com.itl.iap.mes.provider.config.Constant;
import com.itl.iap.mes.provider.mapper.JobMapper;
import org.apache.commons.lang.StringUtils;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class InitScheduler implements CommandLineRunner {
    @Autowired
    private JobMapper scheduleJobDao;
    @Override
    public void run(String... strings) throws Exception {
        Scheduler scheduler = null;
        try {
            scheduler = new StdSchedulerFactory().getScheduler();
        } catch (SchedulerException e) {
            throw new RuntimeException("初始化定时器失败");
        };
        List<ScheduleJobEntity> scheduleJobList = scheduleJobDao.selectList(new QueryWrapper<ScheduleJobEntity>().eq("state", Constant.ScheduleStatus.NORMAL.getItem()));
        scheduleJobList.removeIf(entity -> {
            if(StringUtils.isNotBlank(entity.getParams())){
                Map<String,Object> map = MapUtils.getStringToMap(entity.getParams());
                Object running = map.get("running");
                if(running != null && Constant.SYS_ADMIN.equals(running.toString())){
                    Date date = CornUtils.getCronToDate(entity.getCronExpression());
                    if(date.getTime()<=new Date().getTime()){
                        return true;
                    }
                }
            }
            return false;
        });

        for(ScheduleJobEntity scheduleJob : scheduleJobList){
            CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, scheduleJob.getJobId());
            //如果不存在，则创建
            if(cronTrigger == null) {
                ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
            }else {
                ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
            }
        }
    }
}
