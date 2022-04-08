package com.itl.iap.report.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AndonStateDto {
    @ApiModelProperty("未签到数")
    private int signNum;
    @ApiModelProperty("处理中数")
    private int runNum;
    @ApiModelProperty("已解除数")
    private int finshedNum;
}
