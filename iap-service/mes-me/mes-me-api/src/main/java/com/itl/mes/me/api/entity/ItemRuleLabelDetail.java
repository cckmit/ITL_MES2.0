package com.itl.mes.me.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 物料规则模板明细表
 *
 * @author yx
 * @date 2021-01-21 14:06:32
 */
@Data
@TableName("me_item_rule_label_detail")
@ApiModel(value = "me_item_rule_label_detail", description = "物料规则模板明细")
@Accessors(chain = true)
public class ItemRuleLabelDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * item_rule_label主键
     */
    @TableField("IRL_BO")
    @ApiModelProperty(value = "item_rule_label主键")
    private String irlBo;
    /**
     * 变量名
     */
    @ApiModelProperty(value = "变量名")
    @TableField("RULE_VAR")
    private String ruleVar;
    /**
     * 对应字段
     */
    @ApiModelProperty(value = "对应字段")
    @TableField("RULE_VAL")
    private String ruleVal;
    /**
     * 是否是自定义字段
     */
    @ApiModelProperty(value = "是否是自定义字段")
    @TableField("IS_CUSTOM")
    private String isCustom;

    /**
     * 标签模板变量
     */
    @ApiModelProperty(value = "模板变量")
    @TableField("TEMPLATE_ARG")
    private  String templateArg;

    /**
     * 类型
     */
    @ApiModelProperty(value = "类型")
    @TableField("TYPE")
    private  String type;


}
