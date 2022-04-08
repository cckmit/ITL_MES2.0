package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value="CustomDataAndValVo",description="自定义数据维护属性和值")
public class CustomDataAndValVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="自定义数据类别")
    private String customDataType;

    @ApiModelProperty(value="数据字段")
    private String cdField;

    @ApiModelProperty(value="字段标签")
    private String cdLabel;

    @ApiModelProperty(value="序列",example = "10" )
    private Integer sequence;

    @ApiModelProperty(value="必填,Y:必填，N:非必填")
    private String isDataRequired;

    @ApiModelProperty( value = "属性" )
    private String attribute;

    @ApiModelProperty( value = "属性值" )
    private String vals;

}
