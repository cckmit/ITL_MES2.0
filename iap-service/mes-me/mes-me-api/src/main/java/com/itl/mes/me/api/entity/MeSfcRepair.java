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
 * Sfc维修
 *
 * @author cuichonghe
 * @date 2021-01-25 14:43:26
 */
@Data
@TableName("me_sfc_repair")
@ApiModel(value = "me_sfc_repair", description = "comments")
public class MeSfcRepair implements Serializable {
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
	@TableField("SITE")
	private String site;
	/**
	 * SN【FK】
	 */
	@ApiModelProperty(value = "SN【FK】")
	@TableField("SFC")
	private String sfc;
	/**
	 * SFC过程 生产过程日志表也要插入,这是插入之后的主键
	 */
	@ApiModelProperty(value = "SFC过程")
	@TableField("WIP_LOG_BO")
	private String wipLogBo;
	/**
	 * SFC不合格记录
	 */
	@ApiModelProperty(value = "SFC不合格记录")
	@TableField("NG_LOG_BO")
	private String ngLogBo;
	/**
	 * 维修原因(字典维护)
	 */
	@ApiModelProperty(value = "维修原因(字典维护)")
	@TableField("REPAIR_REASON")
	private String repairReason;
	/**
	 * 维修方式（字典维护)
	 */
	@ApiModelProperty(value = "维修方式（字典维护)")
	@TableField("REPAIR_METHOD")
	private String repairMethod;
	/**
	 * 替换物料
	 */
	@ApiModelProperty(value = "替换物料")
	@TableField("REPLACE_ITEM_BO")
	private String replaceItemBo;
	/**
	 * 责任单位
	 */
	@ApiModelProperty(value = "责任单位")
	@TableField("DUTY_UNIT")
	private String dutyUnit;
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	@TableField("REMARK")
	private String remark;


	@ApiModelProperty(value = "送修时间")
	@TableField("REPAIR_TIME")
	private Date repairTime;

	@ApiModelProperty(value = "替换物料的SN")
	@TableField("REPLACE_ITEM_SN")
	private String replaceItemSn;

	@ApiModelProperty(value = "不良物料的SN")
	@TableField("NG_ITEM_SN")
	private String ngItemSn;

}
