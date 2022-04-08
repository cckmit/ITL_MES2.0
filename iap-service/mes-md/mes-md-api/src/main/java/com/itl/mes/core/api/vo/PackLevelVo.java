package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel(value="PackLevelVo",description = "保存包装明细用")
public class PackLevelVo implements Serializable {

    @ApiModelProperty(value="PKL:SITE,PACK_NO,SEQ【PK】")
    private String bo;

    @ApiModelProperty(value="包装BO")
    private String packingBo;

    @ApiModelProperty(value="序号")
    private String seq;

    @ApiModelProperty(value="包装级别")
    private String packLevel;

    @ApiModelProperty(value="包装对象")
    private String object;

    @ApiModelProperty(value="包装对象版本")
    private String objectVersion;

    @ApiModelProperty(value="工单")
    private String shopOrder;

    @ApiModelProperty(value="最小数量")
    private BigDecimal minQty;

    @ApiModelProperty(value="最大数量")
    private BigDecimal maxQty;
}
