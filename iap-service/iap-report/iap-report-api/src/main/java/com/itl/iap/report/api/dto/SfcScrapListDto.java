package com.itl.iap.report.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: 颜林琪
 * @Date: 2021/08/31   15:06
 * @Description:
 * @Version 1.0
 */
@Data
public class SfcScrapListDto {
    // 前端传值对象
    @ApiModelProperty("BO")
    private String bo;

    @ApiModelProperty("批次条码")
    private String sfc;

    @ApiModelProperty("工单")
    private String shopOrder;

    @ApiModelProperty("报废数量")
    private Double scrapQty;

    @ApiModelProperty("报废原因")
    private String ncName;

    @ApiModelProperty("报废备注")
    private String contents;

}
