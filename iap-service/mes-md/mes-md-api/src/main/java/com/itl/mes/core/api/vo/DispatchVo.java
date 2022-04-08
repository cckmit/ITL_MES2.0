package com.itl.mes.core.api.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DispatchVo {
    private String dispatchCode;
    private String shopOrder;
    private String operationOrder;
    private String itemBo;
    private String item;
    private String itemName;
    private String itemDesc;
    private BigDecimal operationOrderQty;
    private String workShop;
    private BigDecimal dispatchCodeQty;
    private String device;
    private BigDecimal dispatchQty;
}
