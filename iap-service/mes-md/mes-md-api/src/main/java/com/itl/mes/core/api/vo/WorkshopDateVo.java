package com.itl.mes.core.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "WorkshopDateVo",description = "车间日历明细")
public class WorkshopDateVo implements Serializable{

    @ApiModelProperty(value="工厂【UK-】")
    private String site;

    @ApiModelProperty(value="车间【UK-】")
    private String workShop;

    @ApiModelProperty(value="班次")
    private String shift;

    @ApiModelProperty(value="班次")
    private String shiftName;

    @ApiModelProperty(value="生产日期")
    private String productDate;

    @ApiModelProperty(value="开始时间")
    private String startTime;

    @ApiModelProperty(value="结束时间")
    private String endTime;

    @ApiModelProperty(value="是否加班（1是，0否）")
    private String isOvertime;

    @ApiModelProperty(value="修改时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "GMT+8"
    )
    private Date modifyDate;


}
