package com.itl.iap.mes.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "SfcDto",description = "Sfc打印")
public class SfcDto {

    @ApiModelProperty(value = "条码编号")
   private List<String> sfc;

    @ApiModelProperty(value = "标签编号")
   private String labelId;
}
