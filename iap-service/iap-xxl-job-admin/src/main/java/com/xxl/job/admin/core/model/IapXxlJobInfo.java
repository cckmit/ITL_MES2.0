package com.xxl.job.admin.core.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 调度扩展信息表： 用于保存XXL-JOB调度任务的扩展信息，如任务分组、任务名、机器地址、执行器、执行入参和报警邮件等等
 *
 * @author xuxueli
 * @date 2016-1-12
 * @since jdk1.8
 */
@Accessors(chain = true)
@TableName("xxl_job_info")
public class IapXxlJobInfo {

    /**
     * 主键ID
     */
    @TableId
    private int id;

    /**
     * 执行器主键ID
     */
    @TableField("job_group")
    private int jobGroup;

    /**
     * 任务执行CRON表达式
     */
    @TableField("job_cron")
    private String jobCron;

    /**
     * 描述
     */
    @TableField("job_desc")
    private String jobDesc;

    /**
     * 添加时间
     */
    @TableField("add_time")
    private Date addTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 负责人
     */
    @TableField("author")
    private String author;

    /**
     * 报警邮件
     */
    @TableField("alarm_email")
    private String alarmEmail;

    /**
     * 执行器路由策略
     */
    @TableField("executor_route_stategy")
    private String executorRouteStrategy;

    /**
     * 执行器，任务Handler名称
     */
    @TableField("executor_handler")
    private String executorHandler;

    /**
     * 执行器，任务参数
     */
    @TableField("executor_param")
    private String executorParam;

    /**
     * 阻塞处理策略
     */
    @TableField("executor_block_stategy")
    private String executorBlockStrategy;

    /**
     * 任务执行超时时间，单位秒
     */
    @TableField("executor_timeout")
    private int executorTimeout;

    /**
     * 失败重试次数
     */
    @TableField("executor_fail_retry_count")
    private int executorFailRetryCount;

    /**
     * GLUE类型	#com.xxl.job.core.glue.GlueTypeEnum
     */
    @TableField("glue_type")
    private String glueType;

    /**
     * GLUE源代码
     */
    @TableField("glue_source")
    private String glueSource;

    /**
     * GLUE备注
     */
    @TableField("glue_remark")
    private String glueRemark;

    /**
     * GLUE更新时间
     */
    @TableField("glue_updatetime")
    private Date glueUpdatetime;

    /**
     * 子任务ID，多个逗号分隔
     */
    @TableField("child_job_id")
    private String childJobId;

    /**
     * 调度状态：0-停止，1-运行
     */
    @TableField("trigger_status")
    private int triggerStatus;

    /**
     * 上次调度时间
     */
    @TableId("trigger_last_time")
    private long triggerLastTime;

    /**
     * 下次调度时间
     */
    @TableField("trigger_next_time")
    private long triggerNextTime;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(int jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobCron() {
        return jobCron;
    }

    public void setJobCron(String jobCron) {
        this.jobCron = jobCron;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAlarmEmail() {
        return alarmEmail;
    }

    public void setAlarmEmail(String alarmEmail) {
        this.alarmEmail = alarmEmail;
    }

    public String getExecutorRouteStrategy() {
        return executorRouteStrategy;
    }

    public void setExecutorRouteStrategy(String executorRouteStrategy) {
        this.executorRouteStrategy = executorRouteStrategy;
    }

    public String getExecutorHandler() {
        return executorHandler;
    }

    public void setExecutorHandler(String executorHandler) {
        this.executorHandler = executorHandler;
    }

    public String getExecutorParam() {
        return executorParam;
    }

    public void setExecutorParam(String executorParam) {
        this.executorParam = executorParam;
    }

    public String getExecutorBlockStrategy() {
        return executorBlockStrategy;
    }

    public void setExecutorBlockStrategy(String executorBlockStrategy) {
        this.executorBlockStrategy = executorBlockStrategy;
    }

    public int getExecutorTimeout() {
        return executorTimeout;
    }

    public void setExecutorTimeout(int executorTimeout) {
        this.executorTimeout = executorTimeout;
    }

    public int getExecutorFailRetryCount() {
        return executorFailRetryCount;
    }

    public void setExecutorFailRetryCount(int executorFailRetryCount) {
        this.executorFailRetryCount = executorFailRetryCount;
    }

    public String getGlueType() {
        return glueType;
    }

    public void setGlueType(String glueType) {
        this.glueType = glueType;
    }

    public String getGlueSource() {
        return glueSource;
    }

    public void setGlueSource(String glueSource) {
        this.glueSource = glueSource;
    }

    public String getGlueRemark() {
        return glueRemark;
    }

    public void setGlueRemark(String glueRemark) {
        this.glueRemark = glueRemark;
    }

    public Date getGlueUpdatetime() {
        return glueUpdatetime;
    }

    public void setGlueUpdatetime(Date glueUpdatetime) {
        this.glueUpdatetime = glueUpdatetime;
    }

    public String getChildJobId() {
        return childJobId;
    }

    public void setChildJobId(String childJobId) {
        this.childJobId = childJobId;
    }

    public int getTriggerStatus() {
        return triggerStatus;
    }

    public void setTriggerStatus(int triggerStatus) {
        this.triggerStatus = triggerStatus;
    }

    public long getTriggerLastTime() {
        return triggerLastTime;
    }

    public void setTriggerLastTime(long triggerLastTime) {
        this.triggerLastTime = triggerLastTime;
    }

    public long getTriggerNextTime() {
        return triggerNextTime;
    }

    public void setTriggerNextTime(long triggerNextTime) {
        this.triggerNextTime = triggerNextTime;
    }
}
