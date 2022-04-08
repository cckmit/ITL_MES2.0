package com.itl.mes.core.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "OutStationDevice",description = "出站设备记录")
public class OutStationDevice {

    @ApiModelProperty(value = "设备bo")
    private String deviceBo;

    @ApiModelProperty(value = "当前设备出站数量")
    private BigDecimal outStationQty;

    private String device;

}
