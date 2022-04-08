package com.itl.mes.pp.api.dto.scheduleplan;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "CreateResourcesCalendarDTO",description = "资源日历保存请求实体")
public class CreateResourcesCalendarDTO {

    @ApiModelProperty(value = "日期集合")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private List<Date> dates;


    @ApiModelProperty(value = "工厂")
    private String site;

    @ApiModelProperty(value = "工厂名称")
    private String siteName;

    @ApiModelProperty(value = "车间名称")
    private String workShopName;

    @ApiModelProperty(value = "资源名称")
    private String resourceName;

    @ApiModelProperty(value = "车间BO")
    private String workShopBo;


    @ApiModelProperty(value = "资源BO")
    private String resourceBo;

    @ApiModelProperty(value = "类型,1为工厂，2为车间，3为产线，4为设备，5为用户")
    private String resourceType;

    @ApiModelProperty(value = "班次集合")
    private List<ClassFrequencyDTO> classFrequencyDTOList;

}
