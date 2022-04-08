package com.itl.iap.mes.provider.utils;
import com.itl.iap.mes.api.entity.ScheduleJobEntity;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import java.lang.reflect.Method;

public class JobCon extends QuartzJobBean {
	private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        ScheduleJobEntity scheduleJob = (ScheduleJobEntity) context.getMergedJobDataMap()
        		.get(ScheduleJobEntity.JOB_PARAM_KEY);

		//任务开始时间
		long startTime = System.currentTimeMillis();
        try {
            //执行任务
        	logger.debug("任务准备执行，任务ID：" + scheduleJob.getJobId());
			Object target = SpringContextUtils.getBean(scheduleJob.getBeanName());
			Method method = target.getClass().getDeclaredMethod("run", String.class);
			method.invoke(target, scheduleJob.getParams());
			//任务执行总时长
			long times = System.currentTimeMillis() - startTime;

		} catch (Exception e) {


		}finally {

		}
    }
}
