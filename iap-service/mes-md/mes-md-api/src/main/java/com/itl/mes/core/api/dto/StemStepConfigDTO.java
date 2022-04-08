package com.itl.mes.core.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class StemStepConfigDTO {
    @ApiModelProperty("工序BO")
    private String operationBo;
    @ApiModelProperty("代码类型")
    private String codeRuleType;
    @ApiModelProperty("标签id")
    private String labelId;



    private String operationName;
    private String operation;

}
