//package com.itl.mes.andon.provider.utils;
//
//
//import com.itl.mes.andon.api.entity.ScheduleJobEntity;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.scheduling.quartz.QuartzJobBean;
//
//import java.lang.reflect.Method;
//
//public class JobCon extends QuartzJobBean {
//	private Logger logger = LoggerFactory.getLogger(getClass());
//
//	private String name;
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	@Override
//	protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//		System.out.println("Quartz ---->  Hello, " + this.name);
//	}
//}
