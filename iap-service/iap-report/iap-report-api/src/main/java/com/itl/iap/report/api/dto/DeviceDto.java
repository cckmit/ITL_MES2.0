package com.itl.iap.report.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeviceDto {
    private  Page page;
    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    @ApiModelProperty("设备编号")
    private String device;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("车间名称")
    private String workShopName;

    @ApiModelProperty("产线名称")
    private String productLineName;

    @ApiModelProperty("当前状态")
    private String fmachState;
}
