package com.itl.iap.report.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OperationVo {
    @ApiModelProperty("工序名称")
    private String name;

    @ApiModelProperty("状态")
    private String state;

    @ApiModelProperty("日期")
    private String date;

    @ApiModelProperty("具体时间")
    private Date time;

    @ApiModelProperty("下一个工序")
    private String to;

    @ApiModelProperty("完工率")
    private BigDecimal completeProportion;
}
