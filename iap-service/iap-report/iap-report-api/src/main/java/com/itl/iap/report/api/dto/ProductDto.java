package com.itl.iap.report.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProductDto {
    @ApiModelProperty("分页信息")
    private Page page;

    @ApiModelProperty("车间名称")
    private String workShopName;

    @ApiModelProperty("产线名称")
    private String productLineName;

    @ApiModelProperty("工序BO")
    private String operationBo;

    @ApiModelProperty("物料BO")
    private String itemBo;

    @ApiModelProperty("开始日期")
    private String startDate;

    @ApiModelProperty("结束日期")
    private String endDate;

}
