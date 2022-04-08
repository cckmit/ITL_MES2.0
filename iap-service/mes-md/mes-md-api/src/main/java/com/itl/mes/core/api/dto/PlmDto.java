package com.itl.mes.core.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PlmDto {
    @ApiModelProperty("工序编码")
    private String operation;

    @ApiModelProperty("工步编码")
    private String workStepCode;

    @ApiModelProperty("工艺路线编码")
    private String router;

    @ApiModelProperty("物料编码")
    private String item;

    @ApiModelProperty("物料版本或工艺路线版本")
    private String version;
}
