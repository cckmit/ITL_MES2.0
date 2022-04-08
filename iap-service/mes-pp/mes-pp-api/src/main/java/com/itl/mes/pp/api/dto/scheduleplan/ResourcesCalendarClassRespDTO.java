package com.itl.mes.pp.api.dto.scheduleplan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "ResourcesCalendarClassRespDTO",description = "资源日历的班制班次信息返回实体")
public class ResourcesCalendarClassRespDTO {


    @ApiModelProperty(value = "班制Id")
    private String classSystemId;

    @ApiModelProperty(value = "班制名称")
    private String classSystemName;

    @ApiModelProperty(value = "班次信息集合")
    private List<ClassFrequencyDTO> classFrequencyDTOList;

}
