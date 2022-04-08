package com.itl.mes.pp.api.dto.scheduleplan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ClassSystemFrequencyDTO",description = "数据库查询班制班次实体")
public class ClassSystemFrequencyDTO {

    @ApiModelProperty(value = "班制id或班次id")
    private String id;

    @ApiModelProperty(value = "班制名称或班次名称")
    private String name;

    @ApiModelProperty(value = "级别，区分班制或班次")
    private String level;

    @ApiModelProperty(value = "班制ID")
    private String classSystemId;

    @ApiModelProperty(value = "开始时间")
    private String startDateStr;

    @ApiModelProperty(value = "结束时间")
    private String endDateStr;

}
