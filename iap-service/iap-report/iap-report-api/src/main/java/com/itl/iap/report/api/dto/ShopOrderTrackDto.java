package com.itl.iap.report.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ShopOrderTrackDto {
    @ApiModelProperty("分页信息")
    private Page page;

    @ApiModelProperty("工单编号")
    private String shopOrder;

    @ApiModelProperty("物料")
    private String item;

    @ApiModelProperty("计划开始时间")
    private String startDate;

    @ApiModelProperty("计划完成时间")
    private String endDate;

    @ApiModelProperty("订购单")
    private String customerOrder;

    @ApiModelProperty("物料名称")
    private String itemName;

    @ApiModelProperty("物料规格")
    private String itemDesc;

    @ApiModelProperty("作用域1-看板")
    private String actionAcope;

    @ApiModelProperty("工单状态")
    private String state;
}
