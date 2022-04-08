package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sky,
 * @date 2019/6/3
 * @time 10:46
 */
@Data
@ApiModel(value = "ListParameterVo", description = "保存数据列表参数使用")
public class ListParameterVo implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value="序列")
    private Integer sequence;

    @ApiModelProperty(value="参数值【UK】")
    private String fieldValue;

    @ApiModelProperty(value="参数名")
    private String fieldName;

    @ApiModelProperty(value="默认值")
    private String isDefault;
}
