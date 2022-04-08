package com.itl.iap.report.api.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PlanReachedMakingDetailsVo {
    private String operation;
    private String operationName;
    private BigDecimal makingQty;
}
