package com.itl.mes.pp.api.dto.scheduleplan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liuchenghao
 * @date 2020/12/1 10:28
 */
@Data
@ApiModel(value = "WorkShopUnderResourcesRespDTO",description = "车间下的资源信息")
public class WorkShopUnderResourcesRespDTO {


  @ApiModelProperty(value = "BO,相当于ID，包含设备ID，产线ID，用户ID")
  private String bo;

  @ApiModelProperty(value = "名称，包含设备，产线，用户")
  private String name;

  @ApiModelProperty(value = "类型，3代表产线，4代表设备，5代表用户")
  private String type;


}
