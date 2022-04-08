package com.itl.mes.pp.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 崔翀赫
 * @date 2020/12/17$
 * @since JDK1.8
 */
@Data
@ApiModel("机台产品产能表查询条件")
public class MachineProductCapacityDto {
  @ApiModelProperty(value = "车间")
  String workShop;
  @ApiModelProperty(value = "产线")
  String productLineBo;
  @ApiModelProperty(value = "物料编码")
  String item;

  @ApiModelProperty(value = "额定产能是否为空 true null false notnull")
  boolean isNull;
  @ApiModelProperty(value = "每页显示条数")
  int limit;
  @ApiModelProperty(value = "页数")
  int page;
}
