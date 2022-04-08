package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "线圈不良登记记录Vo")
public class StepNcRecordVo {
    @ApiModelProperty(value = "不良代码")
    private String ncCode;
    @ApiModelProperty(value = "不良项目")
    private String ncProject;
    @ApiModelProperty(value = "不良数量")
    private BigDecimal ncQty;
    @ApiModelProperty(value = "不良类型（0：不良 1：报废）")
    private String ncType;
    @ApiModelProperty(value = "不良工步BO")
    private String ncWorkStepBo;
    @ApiModelProperty(value = "不良工步编码")
    private String ncWorkStepCode;
    @ApiModelProperty(value = "不良人")
    private String personLiable;
    @ApiModelProperty(value = "不良人名称")
    private String personLiableName;
    private String insideNo;
}
