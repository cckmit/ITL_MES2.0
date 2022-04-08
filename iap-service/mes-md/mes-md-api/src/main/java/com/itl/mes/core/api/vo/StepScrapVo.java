package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StepScrapVo {
    private String sfc;
    private String shopOrder;
    private String item;
    private String itemName;
    private String scrapOperation;
    private String scrapOperationName;
    private String scrapOperationBo;
    private String scrapStation;
    private String scrapStationName;
    private String scrapStationBo;
    private String userBo;
    private String userName;
    private String state;
    @ApiModelProperty("最大报废数,也就是当前sfc的完成数")
    private BigDecimal maxScrapQty;
    private String routerBo;
    private String currentOperationBo;

    private String ncCodeBo;
    @ApiModelProperty("本次登记报废数")
    private BigDecimal scrapQty;
}
