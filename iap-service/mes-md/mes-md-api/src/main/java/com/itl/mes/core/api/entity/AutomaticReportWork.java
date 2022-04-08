package com.itl.mes.core.api.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AutomaticReportWork {
    private String workStepBo;
//    private String workStepName;
    private String stationBo;
    private String stationName;
    private String userBo;
    private String userName;

    private BigDecimal qty;
}
