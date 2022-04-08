package com.itl.iap.report.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("工序产能报表")
public class OperationCapacityVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("车间BO")
    private String workShopBo;
    @ApiModelProperty("车间名称")
    private String workShopDesc;
    @ApiModelProperty("车间编码")
    private String workShop;
    @ApiModelProperty("工序BO")
    private String operationBo;
    @ApiModelProperty("工序名称")
    private String operationName;
    @ApiModelProperty("工序编码")
    private String operation;
    @ApiModelProperty("物料BO")
    private String itemBo;
    @ApiModelProperty("物料编码")
    private String item;
    @ApiModelProperty("物料名称")
    private String itemName;
    @ApiModelProperty("物料描述")
    private String itemDesc;
    @ApiModelProperty("出站数量")
    private BigDecimal outStationQty;
    @ApiModelProperty("生产时长")
    private String productTime;
    @ApiModelProperty("产能（出站数量/生产时长）")
    private BigDecimal capacity;
}
