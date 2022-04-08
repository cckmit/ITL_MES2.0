package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "WorkshopCalendarVo",description = "保存车间日历用")
public class WorkshopCalendarVo implements Serializable{

    @ApiModelProperty(value="车间日历集合")
    private List<WorkshopDateVo> workshopDateVoList;

    @ApiModelProperty(value="班次明细集合")
    private List<CalendarShiftVo> calendarShiftVos;

    @ApiModelProperty(value="保存:日期集合/查询:Y或N")
    private List<String> period;

    @ApiModelProperty(value = "开始日期")
    private String startPeriod;

    @ApiModelProperty(value = "结束日期")
    private String endPeriod;


    @ApiModelProperty(value="车间【UK-】")
    private String workshop;
}
