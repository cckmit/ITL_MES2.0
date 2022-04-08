package com.itl.iap.report.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class DeviceStateVo {
    @ApiModelProperty("设备编码")
    private String device;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("设备描述")
    private String deviceDesc;

    @ApiModelProperty("车间名称")
    private String workShopName;

    @ApiModelProperty("车间名称")
    private String productLineName;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;

    @ApiModelProperty("持续时间")
    private String continuedTime;

    @ApiModelProperty("机器状态编码")
    private String fmachState;

    @ApiModelProperty("状态")
    private String state;

    @ApiModelProperty("count")
    private int count;
}
