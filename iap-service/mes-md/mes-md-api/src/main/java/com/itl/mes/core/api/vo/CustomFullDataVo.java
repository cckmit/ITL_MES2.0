package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;


@Data
@ApiModel(value="CustomFullDataVo",description="自定义数据维护")
public class CustomFullDataVo implements Serializable {

   private static final long serialVersionUID = 1L;

   @ApiModelProperty(value="自定义数据类别")
   @NotBlank
   private String customDataType;

   List<CustomDataVo> customDataVoList;

}