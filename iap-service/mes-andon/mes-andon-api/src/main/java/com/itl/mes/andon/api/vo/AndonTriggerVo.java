package com.itl.mes.andon.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

/**
 * @auth liuchenghao
 * @date 2020/12/22
 */
@Data
@ApiModel(value = "AndonTriggerVo",description = "安灯触发灯箱返回实体")
public class AndonTriggerVo {

    @ApiModelProperty(value = "用户姓名")
    private String userName;


    @ApiModelProperty(value = "灯箱BO")
    private String andonBoxBo;

    @ApiModelProperty(value = "灯箱名称")
    private String boxName;

    @ApiModelProperty(value = "安灯信息集合")
    private Set<AndonTriggerAndonVo> andonTriggerAndonVos;

}
