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
 * 条码类型
 *
 * @author cuichonghe
 * @date 2021-01-25 14:43:26
 */
@Data
@TableName("me_sn_type")
@ApiModel(value = "me_sn_type", description = "comments")
public class MeSnType implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@TableId
	@ApiModelProperty(value = "编号")
	@TableField("BO")
	private String bo;
	/**
	 * SN条码类型名称
	 */
	@ApiModelProperty(value = "SN条码类型名称")
	@TableField("SN_TYPE_NAME")
	private String snTypeName;

}
