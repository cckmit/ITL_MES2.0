package com.itl.iap.report.api.dto;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SfcReportDto",description = "Sfc报表实体返回")
public class SfcReportDto {

    @ApiModelProperty("分页信息")
    private Page page;

    @ApiModelProperty("工单号")
    private String shopOrder;

    @ApiModelProperty("工序工单")
    private String operationOrder;

    @ApiModelProperty("工序名称")
    private String operationNames;

    @ApiModelProperty("物料编码")
    private String item;

    @ApiModelProperty("批次条码")
    private String sfc;

    @ApiModelProperty("状态")
    private String state;

    @ApiModelProperty("派工单编码")
    private String dispatchCode;
}
