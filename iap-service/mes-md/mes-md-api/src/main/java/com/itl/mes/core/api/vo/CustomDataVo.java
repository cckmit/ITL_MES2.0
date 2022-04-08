package com.itl.mes.core.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@Data
@ApiModel(value="CustomDataVo",description="自定义数据维护")
public class CustomDataVo implements Serializable {

   private static final long serialVersionUID = 1L;

   @ApiModelProperty(value="自定义数据类别")
   @NotBlank
   private String customDataType;

   @ApiModelProperty(value="数据字段")
   @NotBlank
   private String cdField;

   @ApiModelProperty(value="字段标签")
   private String cdLabel;

   @ApiModelProperty(value="序列",example = "10" )
   @NotNull
   private Integer sequence;

   @ApiModelProperty(value="必填,Y:必填，N:非必填")
   private String isDataRequired;

   @ApiModelProperty(value="是否已使用,Y:是，N:否")
   private String isUsed;

   @ApiModelProperty(value="修改日期")
   @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "GMT+8"
    )
   private Date modifyDate;

}