package com.itl.iap.report.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "SfcWipLogReportDto",description = "生产过程日志报表实体返回")
public class SfcWipLogReportDto {
    @ApiModelProperty("分页信息")
    private Page page;

    @ApiModelProperty("工单号")
    private String shopOrder;

    @ApiModelProperty("工序工单")
    private String operationOrder;

    @ApiModelProperty("工序名称")
    private String operationName;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("设备编码")
    private String device;

    @ApiModelProperty("人员")
    private String realName;

    @ApiModelProperty("批次标签")
    private String sfc;

    @ApiModelProperty("进站时间")
    private String inTime;

    @ApiModelProperty("出站时间")
    private String outTime;

    @ApiModelProperty("物料描述")
    private String itemDesc;

    @ApiModelProperty("物料编码")
    private String item;

}
