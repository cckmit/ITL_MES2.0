package com.itl.mes.me.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 作业指导书分配工序
 *
 * @author yaoxiang
 * @date 2020-12-25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("m_instructor_operation")
@ApiModel(value = "m_instructor_operation", description = "作业指导书分配工序")
public class InstructorOperation implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableField("INSTRUCTOR_BO")
	@ApiModelProperty(value = "作业指导书")
	private String instructorBo;


	@TableField("OPERATION_BO")
	@ApiModelProperty(value = "工序")
	private String operationBo;

}
