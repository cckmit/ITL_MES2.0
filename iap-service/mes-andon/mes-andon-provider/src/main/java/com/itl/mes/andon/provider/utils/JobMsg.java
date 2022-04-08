package com.itl.mes.andon.provider.utils;

import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.mes.andon.api.entity.Grade;
import com.itl.mes.andon.api.entity.ScheduleJobEntity;
import com.itl.mes.andon.provider.common.CommonCode;
import com.itl.mes.andon.provider.config.ScheduleConstant;
import com.itl.mes.andon.provider.exception.CustomException;
import com.itl.mes.andon.provider.mapper.GradeMapper;
import com.itl.mes.andon.provider.mapper.JobMapper;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class  JobMsg {


    @Autowired
    private static GradeMapper gradeMapper;

    @Autowired
    private static JobMapper jobMapper;


}
