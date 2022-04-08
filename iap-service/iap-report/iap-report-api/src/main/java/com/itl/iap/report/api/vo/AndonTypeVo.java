package com.itl.iap.report.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AndonTypeVo {

    @ApiModelProperty("安灯类型名称")
    private String andonTypeName;

    @ApiModelProperty("设备编码")
    private String device;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("个数")
    private int count;

    @ApiModelProperty("总时长")
    private int sumTime;

    @ApiModelProperty("平均时长")
    private double avgTime;
}
