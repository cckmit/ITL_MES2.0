package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ReportWorkListDto {
    @ApiModelProperty("分页信息")
    private Page page;

    @ApiModelProperty("报工bo（修改时传入）")
    private String bo;

    @ApiModelProperty("报工数量（修改时传入）")
    private BigDecimal qty;

    @ApiModelProperty("差异数量（修改时传入）")
    private BigDecimal difQty;

    @ApiModelProperty("状态，0：可修改 1:不能修改")
    private String state;

    @ApiModelProperty("批次条码")
    private String sfc;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    @ApiModelProperty("工序Bo")
    private String operationBo;

    @ApiModelProperty("工序工单")
    private String operationOrder;

    @ApiModelProperty("工单编码")
    private String shopOrder;

    @ApiModelProperty("操作员")
    private String userName;



    @ApiModelProperty("车间编码")
    private String workShop;

    @ApiModelProperty("工步bo")
    private String workStepCodeBo;

    @ApiModelProperty("工步编码")
    private String workStepCode;
}
