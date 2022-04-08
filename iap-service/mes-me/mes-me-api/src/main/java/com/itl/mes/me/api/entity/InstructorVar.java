package com.itl.mes.me.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 作业指导书 SOP表
 *
 * @author yaoxiang
 * @date 2020-12-25
 */
@Data
@TableName("m_instructor_var")
@ApiModel(value = "m_instructor_var", description = "指导书变量")
@Accessors(chain = true)
public class InstructorVar implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * INSTRUCTOR_VAR:SITE,NAME,VERSION【PK】
	 */
	@TableId(value = "BO", type = IdType.INPUT)
	@ApiModelProperty(value = "INSTRUCTOR_VAR:SITE,VARCODE,VERSION【PK】")
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
	 * 变量CODE
	 */
	@ApiModelProperty(value = "变量CODE")
	@TableField("VAR_CODE")
	private String varCode;
	/**
	 * 变量Type
	 */
	@ApiModelProperty(value = "变量类型")
	@TableField("VAR_TYPE")
	private String varType;
	/**
	 * 变量描述
	 */
	@ApiModelProperty(value = "变量描述")
	@TableField("VAR_DESC")
	private String varDesc;
	/**
	 * 默认值
	 */
	@ApiModelProperty(value = "默认值")
	@TableField("VAR_DEFAULT")
	private String varDefault;

}
