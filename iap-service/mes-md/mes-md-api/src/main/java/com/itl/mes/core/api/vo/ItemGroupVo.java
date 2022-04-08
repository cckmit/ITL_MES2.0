package com.itl.mes.core.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value="ItemGroupVo",description="物料组维护")
public class ItemGroupVo implements Serializable {

   private static final long serialVersionUID = 1L;

   @ApiModelProperty(value="物料组")
   @NotBlank
   private String itemGroup;

   @ApiModelProperty(value="物料组名称")
   private String groupName;

   @ApiModelProperty(value="物料组描述")
   private String groupDesc;

   @ApiModelProperty(value="已分配物料")
   private List<ItemNameDescVo> assignedItemList;

   @ApiModelProperty( value = "自定义数据属性和属性值" )
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