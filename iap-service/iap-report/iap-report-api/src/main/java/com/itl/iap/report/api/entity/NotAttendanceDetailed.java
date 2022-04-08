package com.itl.iap.report.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("缺勤明细")
public class NotAttendanceDetailed {
    @ApiModelProperty("用户账号")
    private String userBo;
    @ApiModelProperty("用户名称")
    private String userName;
    @ApiModelProperty("连续缺勤天数")
    private String continuityAbsenceFromDutyDays;
}
