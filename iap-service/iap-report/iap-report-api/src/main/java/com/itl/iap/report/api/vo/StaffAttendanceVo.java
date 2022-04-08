package com.itl.iap.report.api.vo;

import com.itl.iap.report.api.entity.NotAttendanceDetailed;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("员工出勤VO")
public class StaffAttendanceVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("车间描述")
    private String workShopDesc;
    @ApiModelProperty("产线描述")
    private String productLineDesc;
    @ApiModelProperty("日期")
    private String attendanceDate;
    @ApiModelProperty("总人数")
    private String totalPeoples;
    @ApiModelProperty("出勤人数")
    private String absenceFromDutyPeoples;
    @ApiModelProperty("出勤率")
    private String attendance;
    @ApiModelProperty("缺勤明细")
    private List<NotAttendanceDetailed> notAttendanceDetailedList;
    @ApiModelProperty("缺勤总人数")
    private int notAttendancePeoples;

    private String workShopBo;
    private String productLineBo;
}
