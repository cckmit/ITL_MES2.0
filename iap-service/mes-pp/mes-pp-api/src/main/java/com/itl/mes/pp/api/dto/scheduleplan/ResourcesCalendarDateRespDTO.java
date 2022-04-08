package com.itl.mes.pp.api.dto.scheduleplan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ResourcesCalendarDateRespDTO",description = "资源日历时间返回实体")
public class ResourcesCalendarDateRespDTO {

    @ApiModelProperty(value = "天")
    private Integer day;

    @ApiModelProperty(value = "状态")
    private String state;

}
