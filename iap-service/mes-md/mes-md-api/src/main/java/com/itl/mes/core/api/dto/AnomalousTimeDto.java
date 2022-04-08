package com.itl.mes.core.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(value = "AnomalousTimeDto",description = "异常时间接受")
public class AnomalousTimeDto {



    @ApiModelProperty(value="结束时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH",
            timezone = "GMT+8"
    )
    private Date relieveTime;

    @ApiModelProperty(value="开始时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH",
            timezone = "GMT+8"
    )
    private Date triggerTime;

    //时间差
    private BigDecimal time;

}
