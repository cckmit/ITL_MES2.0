package com.itl.mes.core.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "SkipStationDTO",description = "跳站DTO")
public class SkipStationDTO {

    @ApiModelProperty(value = "sfc条码")
    private String sfc;

    @ApiModelProperty(value = "跳站之前的工艺路线bo")
    private String oldRouterBo;

    @ApiModelProperty(value = "跳站之后的工艺路线bo（如果没有更改工艺路线，可以为空）")
    private String newRouterBo;

    @ApiModelProperty(value = "跳站用户")
    private String userBo;

    @ApiModelProperty(value = "跳站的工序bo")
    private String operationBo;

    @ApiModelProperty(value = "备注")
    private String remarks;
}
