package com.itl.mes.me.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 作业指导书内容项
 *
 * @author yaoxiang
 * @date 2020-12-25
 */
@Data
@TableName("m_instructor_item")
@ApiModel(value = "m_instructor_item", description = "作业指导书内容项")
@Accessors(chain = true)
public class InstructorItem implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * INSTRUCTOR_ITEM:SITE,NAME,VERSION【PK】
	 */
	@TableId(value = "BO", type = IdType.INPUT)
	@ApiModelProperty(value = "INSTRUCTOR_ITEM:SITE,INSTRUCTORITEM,VERSION【PK】")
	private String bo;
	/**
	 * 工厂
	 */
	@ApiModelProperty(value = "工厂")
	@TableField("SITE")
	private String site;
	/**
	 * 指导书编号
	 */
	@ApiModelProperty(value = "指导书编号")
	@TableField("INSTRUCTOR_BO")
	private String instructorBo;
	/**
	 * 内容项编号
	 */
	@ApiModelProperty(value = "内容项编号")
	@TableField("INSTRUCTOR_ITEM")
	private String instructorItem;
	/**
	 * 内容项名称
	 */
	@ApiModelProperty(value = "内容项名称")
	@TableField("INSTRUCTOR_ITEM_NAME")
	private String instructorItemName;
	/**
	 * 是否启用
	 */
	@ApiModelProperty(value = "是否启用")
	@TableField("STATE")
	private Integer state;
	/**
	 * 颜色
	 */
	@ApiModelProperty(value = "颜色")
	@TableField("COLOR")
	private String color;
	/**
	 * 是否默认显示
	 */
	@ApiModelProperty(value = "是否默认显示")
	@TableField("DEFAULT_SHOW")
	private Integer defaultShow;

	@ApiModelProperty(value = "建档日期")
	@TableField("CREATE_DATE")
	@JsonFormat(
			pattern = "yyyy-MM-dd HH:mm:ss.SSS",
			timezone = "GMT+8"
	)
	private Date createDate;

	@ApiModelProperty(value = "建档人")
	@TableField("CREATE_USER")
	private String createUser;

	@ApiModelProperty(value = "修改日期")
	@TableField("MODIFY_DATE")
	@JsonFormat(
			pattern = "yyyy-MM-dd HH:mm:ss.SSS",
			timezone = "GMT+8"
	)
	private Date modifyDate;

	@ApiModelProperty(value = "修改人")
	@TableField("MODIFY_USER")
	private String modifyUser;

	public void setObjectSetBasicAttribute( String userId,Date date ){
		this.createUser=userId;
		this.createDate=date;
		this.modifyUser=userId;
		this.modifyDate=date;
	}

}
