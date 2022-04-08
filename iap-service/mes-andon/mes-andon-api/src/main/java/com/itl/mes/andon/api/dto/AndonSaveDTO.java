package com.itl.mes.andon.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "AndonSaveDTO",description = "安灯保存请求实体")
public class AndonSaveDTO {

    @ApiModelProperty(value = "安灯BO")
    private String bo;

    @ApiModelProperty(value = "安灯")
    private String andon;

    @ApiModelProperty(value = "安灯名称")
    private String andonName;

    @ApiModelProperty(value = "安灯描述")
    private String andonDesc;

    @ApiModelProperty(value = "安灯灯箱BO")
    private String andonBoxBo;

    @ApiModelProperty(value = "安灯类型BO")
    private String andonTypeBo;

    @ApiModelProperty(value = "安灯推送BO")
    private String andonPushBo;

    @ApiModelProperty(value = "安灯类型，用于区分何种资源")
    private String andonTypeTag;

    @ApiModelProperty(value = "关联对象BO")
    private String relatedObjectBo;

    @ApiModelProperty(value = "状态，1为启用，0为禁用")
    private String state;
}
