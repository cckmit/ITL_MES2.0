package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "EmployeeVo",description = "查询保存班组成员用")
public class EmployeeVo implements Serializable {

    @ApiModelProperty("员工编号")
    private String employeeCode;

    @ApiModelProperty("员工名称")
    private String name ;
}
