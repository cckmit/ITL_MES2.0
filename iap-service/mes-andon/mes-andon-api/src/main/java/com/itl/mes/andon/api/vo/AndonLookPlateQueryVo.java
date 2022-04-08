package com.itl.mes.andon.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @auth liuchenghao
 * @date 2020/12/25
 */
@Data
@ApiModel(value = "AndonLookPlateQueryVo",description = "安灯看板查询信息")
public class AndonLookPlateQueryVo {

    @ApiModelProperty(value = "资源bo,用作逻辑区分")
    private String bo;

    @ApiModelProperty(value = "资源名称")
    private String resourceName;

    @ApiModelProperty(value = "异常日志ID")
    private String pid;

    @ApiModelProperty(value = "安灯的资源类型")
    private String andonResourceType;


    @ApiModelProperty(value = "安灯日志状态")
    private String state;

}
