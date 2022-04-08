package com.itl.iap.report.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class PlanReachedVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String workShopBo;
    private String workShop;
    private String workName;
    private String itemBo;
    private String item;
    private String itemName;
    private String itemDesc;

    @ApiModelProperty("计划总数")
    private Integer planTotalQty;
    @ApiModelProperty("完成数量")
    private BigDecimal doneTotalQty;
    @ApiModelProperty("入库数量")
    private BigDecimal stockTotalQty;
    @ApiModelProperty("在制数量")
    private BigDecimal makingTotalQty;
    @ApiModelProperty("待生产数量")
    private BigDecimal waitInTotalQty;
}
