package com.itl.mes.pp.api.dto.scheduleplan;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "ResourceCopySaveDTO",description = "资源日历复制DTO")
public class ResourceCopySaveDTO {


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

    @ApiModelProperty(value = "需要复制的资源信息集合")
    private List<ResourceCopyDTO> resourceCopys;

}
