package com.itl.mes.core.api.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class OperationOrderAndQty {
    @ApiModelProperty(value = "工序工单号")
    private String operationOrder;

    @ApiModelProperty(value = "数量")
    private BigDecimal qty;
}
