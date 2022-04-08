package com.itl.iap.report.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AndonWarningVo {

    @ApiModelProperty("车间名称")
    private String workShopName;

    @ApiModelProperty("待签到")
    private int signValue;

    @ApiModelProperty("处理中")
    private int handleValue;

    @ApiModelProperty("已解除")
    private int relieveValue;

}
