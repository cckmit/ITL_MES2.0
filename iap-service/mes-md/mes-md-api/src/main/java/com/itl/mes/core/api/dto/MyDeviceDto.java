package com.itl.mes.core.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "MyDeviceDto",description = "MyDevice查询实体")
public class MyDeviceDto {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "工位BO")
    private String stationBo;

    @ApiModelProperty(value = "设备BO")
    private String deviceBo;

    @ApiModelProperty(value = "设备类型")
    private String deviceType;

    @ApiModelProperty(value = "0：未点检 1：已点检")
    private String spotState;

    @ApiModelProperty(value = "0：未首检 1：待品检 2：已审核")
    private String firstInsState;
}
