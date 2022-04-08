package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MyDeviceInfoVo {
    @ApiModelProperty("工单")
    private String shopOrder;

    @ApiModelProperty("物料编码")
    private String item;

    @ApiModelProperty("物料描述")
    private String itemDesc;

    @ApiModelProperty("当前工单在当前工序完成的数量")
    private BigDecimal doneQty;

    @ApiModelProperty("工单数量")
    private BigDecimal shopOrderQty;
}
