package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sky,
 * @date 2019/6/4
 * @time 10:48
 */
@Data
@ApiModel(value = "DcParameterVo", description = "保存数据收集组参数表使用")
public class DcParameterVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="序列")
    private Integer sequence;

    @ApiModelProperty(value="参数名【UK】")
    private String paramName;

    @ApiModelProperty(value="参数说明")
    private String paramDesc;

    @ApiModelProperty(value="类型")
    private String paramType;

    @ApiModelProperty(value="最大值")
    private Integer maxValue;

    @ApiModelProperty(value="最小值")
    private Integer minValue;

    @ApiModelProperty(value="目标值")
    private Integer tarValue;

    @ApiModelProperty(value="覆盖最大最小值")
    private String chkMaxMinVal;

    @ApiModelProperty(value="状态编号")
    private String state;

    @ApiModelProperty(value="是否允许缺少值")
    private String isRequired;

    @ApiModelProperty(value="布尔值1")
    private String boolean1;

    @ApiModelProperty(value="布尔值2")
    private String boolean2;

    @ApiModelProperty(value="参数单位")
    private String unit;

    @ApiModelProperty(value="数据列表（类型为列表时此项必填）")
    private String dataList;

}
