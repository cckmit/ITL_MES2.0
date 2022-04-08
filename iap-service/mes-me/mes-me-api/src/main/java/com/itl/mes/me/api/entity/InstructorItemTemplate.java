package com.itl.mes.me.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import lombok.Data;

/**
 * 作业指导书内容项模板
 *
 * @author yaoxiang
 * @date 2020-12-25
 */
@Data
@TableName("m_instructor_item_template")
@ApiModel(value = "m_instructor_item_template", description = "指导书内容项模板")
public class InstructorItemTemplate implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 内容项编号
	 */
	@TableId(value = "INSTRUCTOR_ITEM_BO", type = IdType.INPUT)
	@ApiModelProperty(value = "内容项编号")
	@TableField("INSTRUCTOR_ITEM_BO")
	private String instructorItemBo;
	/**
	 * 模板
	 */
	@ApiModelProperty(value = "模板")
	@TableField("TEMPLATE")
	private String template;

}
