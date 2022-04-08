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
 * 调度日志表： 用于保存XXL-JOB任务调度的历史信息，如调度结果、执行结果、调度入参、调度机器和执行器等等
 *
 * @author xuxueli
 * @date 2015-12-19
 * @since jdk1.8
 */
@Accessors(chain = true)
@TableName("xxl_job_log")
public class IapXxlJobLog {

    /**
     * 主键ID
     */
    @TableId
    private long id;

    /**
     * 执行器主键ID
     */
    @TableField("job_group")
    private int jobGroup;

    /**
     * 任务，主键ID
     */
    @TableField("job_id")
    private int jobId;

    /**
     * 执行器地址，本次执行的地址
     */
    @TableField("executor_address")
    private String executorAddress;

    /**
     * 执行器任务handler
     */
    @TableField("executor_handler")
    private String executorHandler;

    /**
     * 执行器任务参数
     */
    @TableField("executor_param")
    private String executorParam;

    /**
     * 执行器任务分片参数，格式如 1/2
     */
    @TableField("executor_sharding_param")
    private String executorShardingParam;

    /**
     * 失败重试次数
     */
    @TableField("executor_fail_retry_count")
    private int executorFailRetryCount;

    /**
     * 调度-时间
     */
    @TableField("trigger_time")
    private Date triggerTime;

    /**
     * 调度-结果
     */
    @TableField("trigger_code")
    private int triggerCode;

    /**
     * 调度-日志
     */
    @TableField("trigger_msg")
    private String triggerMsg;

    /**
     * 执行-时间
     */
    @TableField("handle_time")
    private Date handleTime;

    /**
     * 执行-状态
     */
    @TableField("handle_code")
    private int handleCode;

    /**
     * 执行-日志
     */
    @TableField("handle_msg")
    private String handleMsg;

    /**
     * 告警状态：0-默认、1-无需告警、2-告警成功、3-告警失败
     */
    @TableField("alarm_status")
    private int alarmStatus;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(int jobGroup) {
        this.jobGroup = jobGroup;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getExecutorAddress() {
        return executorAddress;
    }

    public void setExecutorAddress(String executorAddress) {
        this.executorAddress = executorAddress;
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

    public String getExecutorShardingParam() {
        return executorShardingParam;
    }

    public void setExecutorShardingParam(String executorShardingParam) {
        this.executorShardingParam = executorShardingParam;
    }

    public int getExecutorFailRetryCount() {
        return executorFailRetryCount;
    }

    public void setExecutorFailRetryCount(int executorFailRetryCount) {
        this.executorFailRetryCount = executorFailRetryCount;
    }

    public Date getTriggerTime() {
        return triggerTime;
    }

    public void setTriggerTime(Date triggerTime) {
        this.triggerTime = triggerTime;
    }

    public int getTriggerCode() {
        return triggerCode;
    }

    public void setTriggerCode(int triggerCode) {
        this.triggerCode = triggerCode;
    }

    public String getTriggerMsg() {
        return triggerMsg;
    }

    public void setTriggerMsg(String triggerMsg) {
        this.triggerMsg = triggerMsg;
    }

    public Date getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

    public int getHandleCode() {
        return handleCode;
    }

    public void setHandleCode(int handleCode) {
        this.handleCode = handleCode;
    }

    public String getHandleMsg() {
        return handleMsg;
    }

    public void setHandleMsg(String handleMsg) {
        this.handleMsg = handleMsg;
    }

    public int getAlarmStatus() {
        return alarmStatus;
    }

    public void setAlarmStatus(int alarmStatus) {
        this.alarmStatus = alarmStatus;
    }

}
