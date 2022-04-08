package com.itl.mes.core.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OkReportWorkDto {

    @ApiModelProperty(value = "sfc")
    private String sfc;

    @ApiModelProperty(value = "用户bo")
    private String userBo;

    @ApiModelProperty(value = "报工数量")
    private BigDecimal workQty;

    @ApiModelProperty(value = "差异数量")
    private BigDecimal difQty;

    @ApiModelProperty(value = "工单bo")
    private String shopOrderBo;

    @ApiModelProperty(value = "工序工单")
    private String operationOrder;

    @ApiModelProperty(value = "物料Bo")
    private String itemBo;

    @ApiModelProperty(value = "工序bo")
    private String operationBo;

    @ApiModelProperty(value = "工步bo")
    private String workStepCodeBo;

    @ApiModelProperty(value = "报工时间（并非当前时间，是时间条件框中的时间）")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date time;
}
