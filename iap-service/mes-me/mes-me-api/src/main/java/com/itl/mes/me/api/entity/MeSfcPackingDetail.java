package com.itl.mes.me.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Sfc包装明细
 *
 * @author cuichonghe
 * @date 2021-01-25 14:43:26
 */
@Data
@TableName("me_sfc_packing_detail")
@ApiModel(value = "me_sfc_packing_detail", description = "comments")
@Accessors(chain = true)
public class MeSfcPackingDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 唯一标识【PK】
	 */
	@TableId(value = "BO", type = IdType.UUID)
	@ApiModelProperty(value = "唯一标识【PK】")
	private String bo;
	/**
	 * 工厂
	 */
	@ApiModelProperty(value = "工厂")
	@TableField("SITE")
	private String site;
	/**
	 * 包装BO
	 */
	@ApiModelProperty(value = "包装BO")
	@TableField("SFC_PACKING_BO")
	private String sfcPackingBo;
	/**
	 * SN号
	 */
	@ApiModelProperty(value = "SN号")
	@TableField("SFC")
	private String sfc;

}
