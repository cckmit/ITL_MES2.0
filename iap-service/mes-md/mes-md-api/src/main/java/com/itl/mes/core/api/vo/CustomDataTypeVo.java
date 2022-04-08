package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;


@Data
@ApiModel(value="CustomDataTypeVo",description="自定义数据维护类型")
public class CustomDataTypeVo implements Serializable {

   private static final long serialVersionUID = 1L;

   @ApiModelProperty(value="自定义数据类型")
   @Length( max = 32 )
   private String customDataType;

   @ApiModelProperty(value="自定义数据类型描述")
   @Length( max = 100 )
   private String customDataTypeDesc;


}