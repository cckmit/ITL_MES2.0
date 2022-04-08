package com.itl.iap.report.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductShopOrderVo {
    @ApiModelProperty("工单BO")
    private String shopOrderBo;

    @ApiModelProperty("工单编号")
    private String shopOrder;

    @ApiModelProperty("工单数量")
    private BigDecimal orderQty;

    @ApiModelProperty("良品数量")
    private int doneQty;

    @ApiModelProperty("报废数量")
    private int scrapQty;

    @ApiModelProperty("不良数量")
    private int ncQty;

}
