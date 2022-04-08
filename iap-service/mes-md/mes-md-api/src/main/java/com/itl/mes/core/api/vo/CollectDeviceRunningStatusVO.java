package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel(value = "CollectDeviceRunningStatusVO",description = "设备运行状态统计")
public class CollectDeviceRunningStatusVO implements Serializable {

    @ApiModelProperty(value="停机")
    private String shut;

    @ApiModelProperty(value="开机")
    private String action;

    @ApiModelProperty(value="待机")
    private String halt;

    @ApiModelProperty(value="异常")
    private String anomaly;
}
