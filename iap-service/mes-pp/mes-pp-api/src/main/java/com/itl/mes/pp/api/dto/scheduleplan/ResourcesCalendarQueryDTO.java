package com.itl.mes.pp.api.dto.scheduleplan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liuchenghao
 * @date 2020/12/1 9:32
 */
@Data
@ApiModel(value = "ResourcesCalendarQueryDTO",description = "资源日历查询请求对象")
public class ResourcesCalendarQueryDTO {



    @ApiModelProperty(value = "工厂")
    private String site;

    @ApiModelProperty(value = "车间BO")
    private String workShopBo;

    @ApiModelProperty(value = "资源BO")
    private String resourceBo;

    @ApiModelProperty(value = "资源类型,1为工厂，2为车间，3为产线，4为设备，5为用户")
    private String resourceType;

    @ApiModelProperty(value = "年")
    private Integer year;

    @ApiModelProperty(value = "月")
    private Integer month;

}
