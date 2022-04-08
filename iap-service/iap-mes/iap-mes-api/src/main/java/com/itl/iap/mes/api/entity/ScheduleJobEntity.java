
package com.itl.iap.mes.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;



@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_schedule_job")
public class ScheduleJobEntity implements Serializable {

	@TableField(exist = false)
	public static final String JOB_PARAM_KEY = "JOB_PARAM_KEY";

	/**
	 * 任务调度参数key
	 */
	private static final long serialVersionUID = -30729856515700265L;

	@TableId("jobId")
	private Long jobId;

	/**
	 * spring bean名称
	 */
	@TableField("beanName")
	private String beanName;

	/**
	 * 参数
	 */
	@TableField("params")
	private String params;

	/**
	 * cron表达式
	 */
	@TableField("cronExpression")
	private String cronExpression;

	/**
	 * 任务状态
	 */
	@TableField("state")
	private Integer state;

	@TableField(exist = false)
	private String stateName;

	/**
	 * 备注
	 */
	@TableField("remark")
	private String remark;

	/**
	 * 创建时间
	 */
	@TableField("createTime")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

}
