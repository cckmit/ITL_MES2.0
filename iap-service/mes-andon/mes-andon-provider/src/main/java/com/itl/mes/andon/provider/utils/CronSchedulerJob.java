//package com.itl.mes.andon.provider.utils;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.itl.mes.andon.api.entity.AndonException;
//import com.itl.mes.andon.api.entity.Grade;
//import com.itl.mes.andon.provider.mapper.ExceptionMapper;
//import com.itl.mes.andon.provider.mapper.GradeMapper;
//import org.aspectj.weaver.ast.And;
//import org.quartz.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.quartz.SchedulerFactoryBean;
//
//@Configuration
//public class CronSchedulerJob {
//    @Autowired
//    private SchedulerFactoryBean schedulerFactoryBean;
//    @Autowired
//    private ExceptionMapper exceptionMapper;
//    @Autowired
//    private GradeMapper gradeMapper;
//
//    private void scheduleJob1(Scheduler scheduler,String exceptionCode) throws SchedulerException {
//        JobDetail jobDetail = JobBuilder.newJob(QuartzJob.class) .withIdentity("job1", "andon").build();
//        // 执行时间 ....
//        AndonException andonException = exceptionMapper.selectOne(new QueryWrapper<AndonException>().eq("exception_code", exceptionCode));
//        if (andonException.getState()!="0"){
//            scheduler.deleteJob(JobKey.jobKey(exceptionCode));
//            return;
//        }
//        Grade grade = gradeMapper.selectOne(new QueryWrapper<Grade>().eq("ANDON_TYPE",andonException.getAndonTypeBo()).eq("WORKSHOP_BO",andonException.getWorkshopBo()));
//        if (grade.getAndonGrade()==1){
//            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/59 0/30 * * * ?");
//            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "andon")
//                    .usingJobData("exceptionCode",exceptionCode).withSchedule(scheduleBuilder).build();
//            scheduler.scheduleJob(jobDetail,cronTrigger);
//        }
//        if (grade.getAndonGrade()==2){
//            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/59 0/30 0/1 * * ? ");
//            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "andon")
//                    .usingJobData("exceptionCode",exceptionCode).withSchedule(scheduleBuilder).build();
//            scheduler.scheduleJob(jobDetail,cronTrigger);
//        }
//    }
//
//    /**
//     * @Author Smith
//     * @Description 同时启动两个定时任务
//     * @Date 16:31 2019/1/24
//     * @Param
//     * @return void
//     **/
//    public void scheduleJobs(String exceptionCode) throws SchedulerException {
//        Scheduler scheduler = schedulerFactoryBean.getScheduler();
//        scheduleJob1(scheduler,exceptionCode);
//    }
//}
