package com.itl.iap.report.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SfcScrapDto {
    @ApiModelProperty("分页信息")
    private Page page;

    @ApiModelProperty("BO")
    private String bo;

    @ApiModelProperty("批次条码")
    private String sfc;

    @ApiModelProperty("工单")
    private String shopOrder;

    @ApiModelProperty("物料名称")
    private String itemName;

    @ApiModelProperty("报废工序名称")
    private String operationName;

    @ApiModelProperty("报废工位名称")
    private String scrapStationName;

    @ApiModelProperty("报废数量")
    private Double scrapQty;

    @ApiModelProperty("报废原因")
    private String ncName;

    @ApiModelProperty("报废人员")
    private String dutyUserName;


}
