package com.itl.mes.andon.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author yaoxiang
 * @date 2020/12/15
 * @since JDK1.8
 */
@Data
@ApiModel(value = "TypeVo", description = "保存安灯类型数据")
public class TypeVo {
    @ApiModelProperty(value = "ANDON_TYPE:SITE,ANDON_TYPE【PK】")
    private String bo;

    @ApiModelProperty(value = "工厂")
    private String site;

    @ApiModelProperty(value = "安灯类型")
    private String andonType;

    @ApiModelProperty(value = "名称")
    private String andonTypeName;

    @ApiModelProperty(value = "描述")
    private String andonTypeDesc;

    @ApiModelProperty(value = "安灯推送")
    private String andonPush;

    @ApiModelProperty(value = "类型标识")
    private String andonTypeTag;

    @ApiModelProperty(value = "状态（已启用1，已禁用0）")
    private String state;

    @ApiModelProperty(value="修改日期")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date modifyDate;

    @ApiModelProperty(value = "异常类型")
    private int abnormalType;

    @ApiModelProperty(value = "是否校验")
    private String isCheck;
}
