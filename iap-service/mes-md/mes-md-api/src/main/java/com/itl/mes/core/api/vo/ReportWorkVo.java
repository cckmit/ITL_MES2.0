package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "ReportWorkVo", description = "报工查询vo")
public class ReportWorkVo {

    @ApiModelProperty(value="sfc")
    private String sfc;

    @ApiModelProperty(value="数量")
    private BigDecimal qty;

    @ApiModelProperty(value="可报工数量")
    private BigDecimal canReportWorkQty;

    @ApiModelProperty(value="报工人员bo")
    private String userBo;

    @ApiModelProperty(value="报工人员姓名")
    private String userName;

    @ApiModelProperty(value="工单bo")
    private String shopOrderBo;

    @ApiModelProperty(value="工单")
    private String shopOrder;

    @ApiModelProperty(value="工序工单")
    private String operationOrder;

    @ApiModelProperty(value="物料bo")
    private String itemBo;

    @ApiModelProperty(value="物料编码")
    private String item;

    @ApiModelProperty(value="物料名称")
    private String itemName;

    @ApiModelProperty(value="物料描述")
    private String itemDesc;

    @ApiModelProperty(value="工序BO")
    private String operationBo;
}
