package com.itl.mes.me.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Sfc装配表
 *
 * @author cuichonghe
 * @date 2021-01-25 14:43:36
 */
@Data
@TableName("me_sfc_assy")
@ApiModel(value = "me_sfc_assy", description = "comments")
@Accessors(chain = true)
public class MeSfcAssy implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(value = "ID", type = IdType.INPUT)
	@ApiModelProperty(value = "主键")
	private String id;
	/**
	 * 工厂
	 */
	@ApiModelProperty(value = "工厂")
	@TableField("SITE")
	private String site;
	/**
	 * SFC日志
	 */
	@ApiModelProperty(value = "SFC日志")
	@TableField("WIP_LOG_BO")
	private String wipLogBo;
	/**
	 * SFC编号【FK】
	 */
	@ApiModelProperty(value = "SFC编号【FK】")
	@TableField("SFC")
	private String sfc;
	/**
	 * 追溯方式
	 */
	@ApiModelProperty(value = "追溯方式")
	@TableField("TRACE_METHOD")
	private String traceMethod;
	/**
	 * 装配组件
	 */
	@ApiModelProperty(value = "装配组件")
	@TableField("COMPONENET_BO")
	private String componenetBo;
	/**
	 * 装配SN
	 */
	@ApiModelProperty(value = "装配SN")
	@TableField("ASSEMBLED_SN")
	private String assembledSn;
	/**
	 * 装配数量
	 */
	@ApiModelProperty(value = "装配数量")
	@TableField("QTY")
	private BigDecimal qty;
	/**
	 * 装配时间
	 */
	@ApiModelProperty(value = "装配时间")
	@TableField("ASSY_TIME")
	@JsonFormat(
			pattern = "yyyy-MM-dd HH:mm:ss.SSS",
			timezone = "GMT+8"
	)
	private Date assyTime;
	/**
	 * 装配用户
	 */
	@ApiModelProperty(value = "装配用户")
	@TableField("ASSY_USER")
	private String assyUser;
	/**
	 * 是否被移除
	 */
	@ApiModelProperty(value = "是否被移除")
	@TableField("REMOVED")
	private Boolean removed;
	/**
	 * 移除时间
	 */
	@ApiModelProperty(value = "移除时间")
	@TableField("REMOVED_TIME")
	@JsonFormat(
			pattern = "yyyy-MM-dd HH:mm:ss.SSS",
			timezone = "GMT+8"
	)
	private Date removedTime;
	/**
	 * 移除用户
	 */
	@ApiModelProperty(value = "移除用户")
	@TableField("REMOVED_USER")
	private String removedUser;
	/**
	 * 组件SN状态
	 */
	@ApiModelProperty(value = "组件SN状态")
	@TableField("COMPONENT_STATE")
	private Integer componentState;

}
