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
 * 调度日志报表：用户存储XXL-JOB任务调度日志的报表，调度中心报表功能页面会用到
 *
 * @author xuxueli
 * @date 2016-5-19
 * @since jdk1.8
 */
@Accessors(chain = true)
@TableName("xxl_job_log_report")
public class IapXxlJobLogReport {

    /**
     * 主键ID
     */
    @TableId
    private int id;

    /**
     * 调度-时间
     */
    @TableField("trigger_day")
    private Date triggerDay;

    /**
     * 运行中-日志数量
     */
    @TableField("running_count")
    private int runningCount;

    /**
     * 执行成功-日志数量
     */
    @TableField("suc_count")
    private int sucCount;

    /**
     * 执行失败-日志数量
     */
    @TableField("fail_count")
    private int failCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTriggerDay() {
        return triggerDay;
    }

    public void setTriggerDay(Date triggerDay) {
        this.triggerDay = triggerDay;
    }

    public int getRunningCount() {
        return runningCount;
    }

    public void setRunningCount(int runningCount) {
        this.runningCount = runningCount;
    }

    public int getSucCount() {
        return sucCount;
    }

    public void setSucCount(int sucCount) {
        this.sucCount = sucCount;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }
}
