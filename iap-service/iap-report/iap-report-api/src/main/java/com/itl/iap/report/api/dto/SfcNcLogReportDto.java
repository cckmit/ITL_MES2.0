package com.itl.iap.report.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SfcNcLogReportDto",description = "Sfc不合格记录表返回实体")
public class SfcNcLogReportDto {
    @ApiModelProperty("分页信息")
    private Page page;

    @ApiModelProperty("工单号")
    private String shopOrder;

    @ApiModelProperty("工序工单")
    private String operationOrder;

    @ApiModelProperty("工序名称")
    private String operationName;

    @ApiModelProperty("责任工序")
    private String dutyOperation;

    @ApiModelProperty("状态")
    private String state;

    @ApiModelProperty("物料编码")
    private String item;

    @ApiModelProperty("批次条码")
    private String sfc;

    @ApiModelProperty("不良原因")
    private String ncName;






}
