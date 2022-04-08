package com.itl.iap.report.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class DeviceResumeVo {
    @ApiModelProperty("车间")
    private String workShopName;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("设备编码")
    private String device;

    @ApiModelProperty("设备类型")
    private String deviceType;

    @ApiModelProperty("操作类型")
    private String operationType;

    @ApiModelProperty("操作人员")
    private String operationUser;

    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date operationTime;

    @ApiModelProperty("状态")
    private String state;

    @ApiModelProperty("操作单号")
    private String code;






}
