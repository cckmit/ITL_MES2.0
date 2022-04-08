package com.itl.mes.pp.api.dto.scheduleplan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @auth liuchenghao
 * @date 2020/12/31
 */
@Data
@ApiModel(value = "TimeDTO",description = "时间段实体，用于生成资源日历的时间段")
public class TimeDTO {

    @ApiModelProperty("开始时间")
    private String startDateStr;

    @ApiModelProperty("结束时间")
    private String endDateStr;

    @ApiModelProperty("相差时间")
    private Double differenceTime;
}
