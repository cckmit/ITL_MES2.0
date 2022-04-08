//package com.itl.mes.andon.provider.utils;
//
//import org.quartz.Job;
//import org.quartz.JobDataMap;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//public class QuartzJob implements Job {
//    private Logger logger = LoggerFactory.getLogger(QuartzJob. class );
//
//    private String name;
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    @Override
//    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//
//        System.out.println("CRON ----> schedule job1 is running ... + " + name + "  ---->  " + dateFormat.format(new Date()));
//    }
//}
