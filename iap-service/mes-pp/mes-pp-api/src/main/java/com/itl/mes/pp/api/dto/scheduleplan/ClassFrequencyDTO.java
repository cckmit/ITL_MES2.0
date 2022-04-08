package com.itl.mes.pp.api.dto.scheduleplan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ClassFrequencyDTO",description = "班次实体")
public class ClassFrequencyDTO {

    @ApiModelProperty(value = "班次Id")
    private String classFrequencyId;

    @ApiModelProperty(value = "班次名称")
    private String classFrequencyName;

    @ApiModelProperty(value = "开始时间")
    private String startDateStr;

    @ApiModelProperty(value = "结束时间")
    private String endDateStr;

    @ApiModelProperty(value = "班制ID")
    private String fatherId;

}
