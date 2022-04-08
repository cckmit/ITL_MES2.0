package com.itl.iap.report.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("员工日产能安灯明细Vo")
public class StaffDailyEnergyAndonDetailsVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("安灯类型名称")
    private String andonTypeName;
    @ApiModelProperty("安灯明细")
    private String andonDetail;
    @ApiModelProperty("触发时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date triggerTime;
    @ApiModelProperty("解除时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date relieveTime;
    @ApiModelProperty("持续时间")
    private String processingTime;

    private String userBo;
}
