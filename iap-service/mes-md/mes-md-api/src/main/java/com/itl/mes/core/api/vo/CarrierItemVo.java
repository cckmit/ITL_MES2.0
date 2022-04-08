package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "CarrierItemVo",description = "保存载具物料明细用")
public class CarrierItemVo {

    @ApiModelProperty(value="CT:SITE,CARRIER_TYPE,SEQ[PK-]")
    private String bo;

    @ApiModelProperty(value="载具类型BO[UK-]")
    private String carrierTypeBo;

    @ApiModelProperty(value="类型")
    private String carrierItemType;

    @ApiModelProperty(value="物料")
    private String item;

    @ApiModelProperty(value="物料版本")
    private String itemVersion;

    @ApiModelProperty(value="物料组")
    private String itemGroup;
}
