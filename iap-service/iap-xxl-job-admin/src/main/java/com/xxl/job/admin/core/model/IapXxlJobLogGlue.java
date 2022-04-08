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
 * 任务GLUE日志：用于保存GLUE更新历史，用于支持GLUE的版本回溯功能
 *
 * @author xuxueli
 * @date 2016-5-19
 * @since jdk1.8
 */
@Accessors(chain = true)
@TableName("xxl_job_log_glue")
public class IapXxlJobLogGlue {

    /**
     * 主键ID
     */
    @TableId
    private int id;

    /**
     * 任务主键ID
     */
    @TableField("job_id")
    private int jobId;

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
     * 添加时间
     */
    @TableField("add_time")
    private Date addTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
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

}
