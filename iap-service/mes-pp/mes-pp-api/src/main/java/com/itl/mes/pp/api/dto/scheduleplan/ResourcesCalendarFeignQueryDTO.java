package com.itl.mes.pp.api.dto.scheduleplan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @auth liuchenghao
 * @date 2021/1/8
 */
@Data
@ApiModel(value = "ResourcesCalendarFeignQueryDTO",description = "资源日历feign查询实体")
public class ResourcesCalendarFeignQueryDTO {

    @ApiModelProperty(value = "开始时间")
    private Date beginDate;

    @ApiModelProperty(value = "结束时间")
    private Date endDate;

    @ApiModelProperty(value = "产线BO")
    private String productLineBo;

}
