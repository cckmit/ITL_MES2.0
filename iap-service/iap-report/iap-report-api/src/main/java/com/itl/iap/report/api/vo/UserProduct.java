package com.itl.iap.report.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserProduct {
    @ApiModelProperty("BO")
    private String bo;

    @ApiModelProperty("车间名称")
    private String workShopName;

    @ApiModelProperty("用户名称")
    private String realName;

    @ApiModelProperty("用户账号")
    private String userName;

    @ApiModelProperty("时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date time;

    @ApiModelProperty("工单编码")
    private String shopOrder;

    @ApiModelProperty("物料编码")
    private String item;

    @ApiModelProperty("物料名称")
    private String itemName;

    @ApiModelProperty("物料描述")
    private String itemDesc;

    @ApiModelProperty("sfc")
    private String sfc;

    @ApiModelProperty("工序名称")
    private String operationName;

    @ApiModelProperty("工步名称")
    private String workStepName;

    @ApiModelProperty("数量")
    private BigDecimal qty;

    @ApiModelProperty("是否改制（0 否认 1 是）")
    private String isReform;
}
