package com.itl.iap.report.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PlanReachedOrderDetailsVo {
    private String shopOrder;
    private String shopOrderBo;
    private Integer orderQty;
    private BigDecimal waitInQty;
    private BigDecimal makingQty;
    private BigDecimal doneQty;
    private BigDecimal stockQty;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date planStartTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date planEndTime;
}
