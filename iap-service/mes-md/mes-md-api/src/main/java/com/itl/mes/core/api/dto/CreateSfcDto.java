package com.itl.mes.core.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "CreateSfcDto",description = "生成条码")
public class CreateSfcDto {
    @ApiModelProperty(value = "标签份数")
    String number;

    @ApiModelProperty(value = "编码规则类型")
    String codeRuleType;

    @ApiModelProperty(value = "批次数量")
    String sfcQty;

    @ApiModelProperty(value = "工序工单号")
    String operationOrder;

    @ApiModelProperty(value = "派工单编码")
    String dispatchCode;
}
