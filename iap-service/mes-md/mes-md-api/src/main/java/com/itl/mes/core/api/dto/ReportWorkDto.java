package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "ReportWorkDto",description = "ReportWorkDto")
public class ReportWorkDto {

    @ApiModelProperty(value = "分页信息")
    private Page page;

    @ApiModelProperty(value = "工步bo")
    private String workStepCodeBo;

    @ApiModelProperty(value = "日期，传入字符串（格式为 yyyy-MM-dd）")
    private String time;

    @ApiModelProperty(value = "工序工单")
    private String operationOrder;

    @ApiModelProperty(value="工单bo")
    private String shopOrderBo;

    @ApiModelProperty(value="工单编码")
    private String shopOrder;

    @ApiModelProperty(value="sfc")
    private String sfc;

    private String operationBo;

    private String startTime;
    private String endTime;

    private String userBo;
}
