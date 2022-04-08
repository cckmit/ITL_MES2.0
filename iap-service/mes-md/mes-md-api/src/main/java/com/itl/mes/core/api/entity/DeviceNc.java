package com.itl.mes.core.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "DeviceNc",description = "设备不良记录")
public class DeviceNc {

    @ApiModelProperty(value = "设备bo")
    private String deviceBo;

    @ApiModelProperty(value = "类型，不良or报废，0：不良 1：报废")
    private String type;

    @ApiModelProperty(value = "不良或报废的数量")
    private BigDecimal qty;

    @ApiModelProperty(value = "不良代码bo")
    private String ncCodeBo;

    @ApiModelProperty(value = "备注")
    private String memo;
}
