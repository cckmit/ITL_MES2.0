package com.itl.mes.me.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * Sfc不合格记录表
 *
 * @author cuichonghe
 * 2021-01-25 14:43:25
 */
@Data
@TableName("me_sfc_nc_log")
@ApiModel(value = "me_sfc_nc_log", description = "comments")
public class MeSfcNcLog implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 唯一标识【PK】
	 */
	@TableId
	@ApiModelProperty(value = "唯一标识【PK】")
	private String bo;
	/**
	 * 工厂
	 */
	@ApiModelProperty(value = "工厂")
	private String site;
	/**
	 * SN【FK】
	 */
	@ApiModelProperty(value = "SN【FK】")
	private String sfc;
	/**
	 * SFC过程
	 */
	@ApiModelProperty(value = "SFC过程")
	private String wipLogBo;
	/**
	 * 是否原材料检测
	 */
	@ApiModelProperty(value = "是否原材料检测")
	private Boolean isRawCheck;
	/**
	 * 组件BO
	 */
	@ApiModelProperty(value = "组件BO")
	private String componentBo;
	/**
	 * 不合格代码BO
	 */
	@ApiModelProperty(value = "不合格代码BO")
	private String ncCodeBo;
	/**
	 * 记录时间
	 */
	@ApiModelProperty(value = "记录时间")
	@TableField("RECORD_TIME")
	private Date recordTime;
	/**
	 * 问题产线
	 */
	@ApiModelProperty(value = "问题产线")
	private String productLineBo;
	/**
	 * 问题工序
	 */
	@ApiModelProperty(value = "问题工序")
	private String operationBo;
	/**
	 * 问题工位
	 */
	@ApiModelProperty(value = "问题工位")
	private String stationBo;
	/**
	 * 问题设备
	 */
	@ApiModelProperty(value = "问题设备")
	private String deviceBo;
	/**
	 * 问题工号
	 */
	@ApiModelProperty(value = "问题工号")
	private String userBo;
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;

}
