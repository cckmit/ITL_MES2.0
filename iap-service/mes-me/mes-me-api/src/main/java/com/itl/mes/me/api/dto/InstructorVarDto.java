package com.itl.mes.me.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yaoxiang
 * @date 2020/12/28
 * @since JDK1.8
 */
@Data
@ApiModel(value = "InstructorVar保存dto")
public class InstructorVarDto implements Serializable {
    @ApiModelProperty(value = "bo")
    private String bo;
    @ApiModelProperty(value = "指导书编号")
    private String instructorBo;
    @ApiModelProperty(value = "变量CODE")
    private String varCode;
    @ApiModelProperty(value = "变量类型")
    private String varType;
    @ApiModelProperty(value = "变量描述")
    private String varDesc;
    @ApiModelProperty(value = "默认值")
    private String varDefault;

}
