package com.itl.mes.pp.api.dto.scheduleplan;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
@ApiModel(value = "ResourcesCalendarRespDTO",description = "资源日历查询返回实体")
public class ResourcesCalendarRespDTO {

    @ApiModelProperty(value = "资源日历ID")
    private String id;

    @ApiModelProperty(value = "资源日历日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date date;

    @ApiModelProperty(value = "工厂名称")
    private String siteName;

    @ApiModelProperty(value = "工厂")
    private String site;

    @ApiModelProperty(value = "车间BO")
    private String workShopBo;


    @ApiModelProperty(value = "资源BO")
    private String resourceBo;

    @ApiModelProperty(value = "车间名称")
    private String workShopName;

    @ApiModelProperty(value = "资源名称")
    private String resourceName;

    @ApiModelProperty(value = "资源类型，3代表产线，4代表设备，5代表用户")
    private String resourceType;

    @ApiModelProperty(value = "班次名称")
    private String classFrequencyName;

    @ApiModelProperty(value = "班制ID")
    private String classSystemId;

    @ApiModelProperty(value = "班次开始时间")
    private String startDateStr;

    @ApiModelProperty(value = "班次结束时间")
    private String endDateStr;

    @ApiModelProperty(value = "是否加班,0为否，1为是")
    private String isWorkOvertime;

    @ApiModelProperty(value = "日")
    private Integer day;

    @ApiModelProperty(value = "班次Id")
    private String classFrequencyId;

    @ApiModelProperty(value = "状态，日历的状态")
    private String state;

    @ApiModelProperty(value = "工时")
    private Double workHour;

    @ApiModelProperty(value = "排序")
    private Integer sort;
}
