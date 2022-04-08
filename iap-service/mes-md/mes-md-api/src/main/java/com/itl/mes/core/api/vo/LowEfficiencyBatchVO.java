package com.itl.mes.core.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(value = "LowEfficiencyBatchVO",description = "低效能批次")
public class LowEfficiencyBatchVO {

    @ApiModelProperty(value="工序")
    private String operationName;

    @ApiModelProperty(value="设备")
    private String deviceName;

    @ApiModelProperty(value="批次条码")
    private String sfc;

    @ApiModelProperty(value="结束时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH",
            timezone = "GMT+8"
    )
    private Date outTime;

    @ApiModelProperty(value="开始时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH",
            timezone = "GMT+8"
    )
    private Date inTime;

    @ApiModelProperty(value="良品数量")
    private BigDecimal doneQty ;

    @ApiModelProperty(value="效率")
    private BigDecimal efficiency ;

    @ApiModelProperty(value="加工时长")
    private BigDecimal time ;

    @ApiModelProperty(value="产线")
    private String productLineDesc ;

    @ApiModelProperty(value="数据日期")
    @JsonFormat(
            pattern = "yyyy-MM-dd",
            timezone = "GMT+8"
    )
    private String day;

    public String scope;
}
