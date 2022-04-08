package com.itl.iap.report.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("工序产能明细VO")
public class OperationCapacityDetailedVo {
    @ApiModelProperty("工单")
    private String shopOrder;
    @ApiModelProperty("sfc")
    private String sfc;
    @ApiModelProperty("用户名字")
    private String userName;
    @ApiModelProperty("用户账号")
    private String userBo;
    @ApiModelProperty("工位编码")
    private String station;
    @ApiModelProperty("工位描述")
    private String stationDesc;
    @ApiModelProperty("进站时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date inTime;
    @ApiModelProperty("出站时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date outTime;
    @ApiModelProperty("完成数")
    private BigDecimal doneQty;
    @ApiModelProperty("不良数")
    private BigDecimal ncQty;
    @ApiModelProperty("报废数")
    private BigDecimal scrapQty;
}
