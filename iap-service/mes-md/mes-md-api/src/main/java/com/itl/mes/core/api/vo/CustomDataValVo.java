package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
@ApiModel( value = "CustomDataValVo", description = "自定义数据维护" )
public class CustomDataValVo implements Serializable {

   private static final long serialVersionUID = 1L;

   @ApiModelProperty( value = "属性" )
   private String attribute;

   @ApiModelProperty( value = "属性值" )
   private String vals;

}