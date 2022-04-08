package com.itl.mes.andon.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @auth liuchenghao
 * @date 2021/3/4
 */
@Data
@ApiModel(value = "AndonTriggerPushUserVo",description = "安灯触发推送人员实体")
public class AndonTriggerPushUserVo {

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

}
