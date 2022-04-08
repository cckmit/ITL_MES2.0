package com.itl.iap.report.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProductVo {
    @ApiModelProperty("车间名称")
    private String workShopName;

    @ApiModelProperty("产线名称")
    private String productLineName;

    @ApiModelProperty("工序名称")
    private String operationName;

    @ApiModelProperty("工序BO")
    private String operationBo;

    @ApiModelProperty("良品数量")
    private int doneQty;

    @ApiModelProperty("报废数量")
    private int scrapQty;

    @ApiModelProperty("不良数量")
    private int ncQty;



}
