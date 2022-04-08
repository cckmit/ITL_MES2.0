package com.itl.mes.core.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "ReworkDto",description = "确认返修dto")
public class ReworkDto {

    @ApiModelProperty(value = "sfc条码")
    private String sfc;

    @ApiModelProperty(value = "返修OK数量")
    private BigDecimal repairQty;

    @ApiModelProperty(value = "报废数量")
    private BigDecimal scrapQty;

    @ApiModelProperty(value = "关联me_sfc_wip_log")
    private String wipLogBo;

    @ApiModelProperty(value = "关联me_sfc_nc_log")
    private String ngLogBo;

    @ApiModelProperty(value = "")
    private String repairReason;

    @ApiModelProperty(value = "维修方法，字典维护")
    private String repairMethod;

    @ApiModelProperty(value = "")
    private String replaceItemBo;

    @ApiModelProperty(value = "责任单位")
    private String dutyUnit;

    @ApiModelProperty(value = "责任工序")
    private String dutyOperation;

    @ApiModelProperty(value = "责任人")
    private String dutyUser;

    @ApiModelProperty(value = "不良代码bo")
    private String ncCodeBo;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "工序编码")
    private String operation;

    @ApiModelProperty(value = "流程ID")
    private String opIds;

    @ApiModelProperty(value = "工序BO")
    private String operationBo;

    private String userBo;

}
