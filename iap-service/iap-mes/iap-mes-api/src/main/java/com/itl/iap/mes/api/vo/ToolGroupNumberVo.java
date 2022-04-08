package com.itl.iap.mes.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @auth liuchenghao
 * @date 2020/12/22
 */
@Data
@ApiModel(value = "ToolGroupNumberVo",description = "工具组编号返回实体")
public class ToolGroupNumberVo {


    @ApiModelProperty(value = "工具组编号BO")
    private String bo;

    @ApiModelProperty(value = "工具组编号")
    private String toolNumber;


    @ApiModelProperty(value = "工具组名称")
    private String toolGroup;


    @ApiModelProperty(value = "工具组编号描述")
    private String tnDesc;

    @ApiModelProperty(value = "数量")
    private Integer qty;

    @ApiModelProperty(value = "状态")
    private String state;



}
