package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "CalendarShiftVo",description = "车间日历保存班次用")
public class CalendarShiftVo implements Serializable {
    @ApiModelProperty(value="班次【UK-】")
    private String shift;

    @ApiModelProperty(value="班次名称")
    private String shiftName;

    @ApiModelProperty(value="班次描述")
    private String shiftDesc;

    @ApiModelProperty(value="是否有效 Y：表示当前班次有效 N：表示当前班次无效 ")
    private String isValid;

    @ApiModelProperty(value="班次时段开始时间")
    private String shiftStartDate;

    @ApiModelProperty(value="班次时段结束时间")
    private String shiftEndDate;

    @ApiModelProperty(value="是否属于当天， Y： 表示当天 N： 表示前一天 ")
    private String isCurrent;
}
