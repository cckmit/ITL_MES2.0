package com.itl.mes.pp.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("p_strategy")
public class Strategy {

    @TableId(type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "排产策略名称")
    @TableField("strategyBz")
    private String strategyBz;

    @ApiModelProperty(value = "排产策略标示")
    @TableField("strategyKey")
    private String strategyKey;

    @ApiModelProperty(value = "是否选择 （1选择 2未选择）")
    @TableField("isUse")
    private Integer isUse;

    @ApiModelProperty(value = "该策略有值就写入到备注")
    @TableField("bz")
    private String bz;

    @ApiModelProperty(value = "工厂")
    @TableField("workshop")
    private String workshop;

    @ApiModelProperty(value = "产线")
    @TableField("productionLine")
    private String productionLine;

    @ApiModelProperty(value = "是否默认（1是 2否）")
    @TableField("isDefault")
    private Integer isDefault;

    @ApiModelProperty(value = "区别于一种策略的标示")
    @TableField("isUnique")
    private String isUnique;
}
