package com.itl.mes.core.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value="ProductLineVo",description="产线保存")
public class ProductLineVo implements Serializable {

   private static final long serialVersionUID = 1L;

   @ApiModelProperty(value="产线")
   private String productLine;

   @ApiModelProperty(value="产线描述")
   private String productLineDesc;

   @ApiModelProperty(value="所属车间")
   private String workShop;

   @ApiModelProperty(value="状态（已启用1，已禁用0）")
   private String state;

   @ApiModelProperty( value = "自定义数据属性和值" )
   private List<CustomDataAndValVo> customDataAndValVoList;

   @ApiModelProperty( value = "自定义数据属性和值，保存时传入" )
   private List<CustomDataValVo> customDataValVoList;

   @ApiModelProperty(value="修改日期")
   @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "GMT+8"
    )
   private Date modifyDate;

}