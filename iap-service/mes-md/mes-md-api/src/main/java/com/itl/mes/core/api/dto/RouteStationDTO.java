package com.itl.mes.core.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "RouteStationDTO",description = "工步配置DTO")
public class RouteStationDTO {
    @ApiModelProperty(value = "工艺路线编码")
    private String processRoute;

    @ApiModelProperty(value = "工艺路线版本")
    private String routeVer;

    @ApiModelProperty(value = "工步编码")
    private String workStepCode;

    @ApiModelProperty(value = "工步名称")
    private String workStepName;

    @ApiModelProperty(value = "工序")
    private String workingProcess;

    @ApiModelProperty(value = "工序名称")
    private String workingProcessName;

    @ApiModelProperty(value = "状态")
    private String state;

    @ApiModelProperty(value = "时效性")
    private String effective;

    @ApiModelProperty(value = "更新人")
    private String updatedBy;
}
