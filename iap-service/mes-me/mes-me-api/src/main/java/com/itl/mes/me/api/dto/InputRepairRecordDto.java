package com.itl.mes.me.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 插入维修记录表
 */
@Data
@ApiModel("维修记录表插入-所需数据")
public class InputRepairRecordDto {

    @ApiModelProperty(value = "维修原因")
    private String repairReason;
    @ApiModelProperty(value = "维修方式")
    private String repairMethod;
    @ApiModelProperty(value = "责任单位")
    private String dutyUnit;

    @ApiModelProperty(value = "不良物料编码")
    private String badItem;
    @ApiModelProperty(value = "不良物料SN")
    private String badItemSn;
    @ApiModelProperty(value = "换修物料SN")
    private String exchangeItem;
    @ApiModelProperty(value = "备注")
    private String remark;

}
