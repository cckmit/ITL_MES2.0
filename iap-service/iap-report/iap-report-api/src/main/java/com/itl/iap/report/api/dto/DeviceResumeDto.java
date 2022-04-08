package com.itl.iap.report.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeviceResumeDto {
    @ApiModelProperty("分页信息")
    private Page page;

    @ApiModelProperty("车间编码")
    private String workShop;

    @ApiModelProperty("开始日期")
    private String startDate;

    @ApiModelProperty("结束日期")
    private String endDate;

    @ApiModelProperty("操作类型")
    private String operationType;

    @ApiModelProperty("设备类型编码")
    private String deviceType;

    @ApiModelProperty("设备名称")
    private String deviceName;

    @ApiModelProperty("设备编码")
    private String device;

    @ApiModelProperty("产线Bo")
    private String productLineBo;
}
