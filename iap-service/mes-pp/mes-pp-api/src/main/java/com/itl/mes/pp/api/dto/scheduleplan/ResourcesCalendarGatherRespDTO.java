package com.itl.mes.pp.api.dto.scheduleplan;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@ApiModel(value = "ResourcesCalendarGatherRespDTO",description = "资源日历汇总返回实体")
public class ResourcesCalendarGatherRespDTO {

    @ApiModelProperty(value = "时间集合,根据前端的年月获取到日的集合，用于显示日历")
    private Set<ResourcesCalendarDateRespDTO> dateResp;

    @ApiModelProperty(value = "班次集合Id，用于回显班次信息")
    private Set<String> classFrequencyIds;

    @ApiModelProperty(value = "班制Id")
    private String classSystemId;

    @ApiModelProperty(value = "资源日历集合，用于显示资源日历信息")
    private List<ResourcesCalendarRespDTO> resourcesCalendarRespDTOList;
}
