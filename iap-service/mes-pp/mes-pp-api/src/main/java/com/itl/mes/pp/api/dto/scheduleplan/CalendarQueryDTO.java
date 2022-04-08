package com.itl.mes.pp.api.dto.scheduleplan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "CalendarQueryDTO",description = "日历查询请求,用于资源日历上的日历显示")
public class CalendarQueryDTO {

    @ApiModelProperty(value = "工厂BO")
    private String site;

    @ApiModelProperty(value = "车间BO")
    private String workShopBo;

    @ApiModelProperty(value = "资源BO")
    private String resourceBo;

    @ApiModelProperty(value = "资源类型，1为工厂，2为车间，3为产线，4为设备，5为用户")
    private String resourceType;

    @ApiModelProperty(value = "年")
    private Integer year;

    @ApiModelProperty(value = "月")
    private Integer month;

}
