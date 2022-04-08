package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ShopOrderReportDTO",description = "工单报表查询")
public class ShopOrderReportDTO {

    @ApiModelProperty(value = "分页信息")
    private Page page;

    @ApiModelProperty(value = "车间")
    private String workShop;

    @ApiModelProperty(value = "物料")
    private String item;

    @ApiModelProperty(value = "工单")
    private String shopOrder;

    @ApiModelProperty(value = "计划开始时间start")
    private String beginStartDate;

    @ApiModelProperty(value = "计划开始时间end")
    private String beginEndDate;

    @ApiModelProperty(value = "计划结束时间start")
    private String finishStartDate;

    @ApiModelProperty(value = "计划结束时间end")
    private String finishEndDate;

    private String stateBo;
}
