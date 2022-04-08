package com.itl.mes.core.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StemReportWorkDto {

    @ApiModelProperty("物料BO")
    private String itemBo;

    @ApiModelProperty("工步BO")
    private String workStepCodeBo;

    @ApiModelProperty("工序BO")
    private String operationBo;

    @ApiModelProperty("工单BO")
    private String shopOrderBo;

    @ApiModelProperty("工序工单")
    private String operationOrder;

    @ApiModelProperty("用户ID")
    private String userBo;

    @ApiModelProperty("报工数量")
    private BigDecimal qty;

    @ApiModelProperty("报工状态（默认为0待确认，1为已确认）")
    private String state;

    @ApiModelProperty("派工单编码")
    private String stepDispatchCode;

    @ApiModelProperty("其他报工人")
    private String otherUser;
}
