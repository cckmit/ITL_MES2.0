package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel(value = "SfcDataStatisticsVo", description = "sfc完成数据统计")
@Data
public class SfcDataStatisticsVo {

    @ApiModelProperty("车间名称")
    private String workShopName;

    @ApiModelProperty("批次条码")
    private String sfc;

    @ApiModelProperty("产品")
    private String itemName;

    @ApiModelProperty("总工序数")
    private String operationCounts;

    @ApiModelProperty("最长工序")
    private String theLongestOperation;

    @ApiModelProperty("最长工序工时")
    private String theLongestOperationTime;

    @ApiModelProperty("良品数")
    private BigDecimal doneQty;

    @ApiModelProperty("不良数")
    private BigDecimal ncQty;

    @ApiModelProperty("不良阈值")
    private String ncThreshold;

    @ApiModelProperty("状态灯（0：红灯 1:绿灯）")
    private String stateLamp;
}