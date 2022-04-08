package com.itl.iap.report.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProductOperationVo {

    @ApiModelProperty("物料BO")
    private String itemBo;

    @ApiModelProperty("物料编码")
    private String item;

    @ApiModelProperty("物料名称")
    private String itemName;

    @ApiModelProperty("物料描述")
    private String itemDesc;

    @ApiModelProperty("良品数量")
    private int doneQty;

    @ApiModelProperty("报废数量")
    private int scrapQty;

    @ApiModelProperty("不良数量")
    private int ncQty;
}
