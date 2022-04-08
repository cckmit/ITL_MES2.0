package com.itl.mes.pp.api.dto.scheduleplan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ResourceCopyDTO",description = "需要复制的资源信息")
public class ResourceCopyDTO {


    @ApiModelProperty(value = "工厂")
    private String copySite;

    @ApiModelProperty(value = "车间BO")
    private String copyWorkShopBo;

    @ApiModelProperty(value = "资源BO")
    private String copyResourceBo;

    @ApiModelProperty(value = "资源类型,1为工厂，2为车间，3为产线，4为设备，5为用户")
    private String copyResourceType;

}
