package com.itl.mes.me.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 物料规则模板
 *
 * @author yx
 * @date 2021-01-21
 */
@Data
@TableName("me_item_rule_label")
@ApiModel(value = "me_item_rule_label", description = "物料规则模板")
@Accessors(chain = true)
public class ItemRuleLabel implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.INPUT)
    @ApiModelProperty(value = "主键")
    private String bo;
    /**
     * 物料
     */
    @ApiModelProperty(value = "物料")
    @TableField("ITEM_BO")
    private String itemBo;
    /**
     * 编码规则
     */
    @ApiModelProperty(value = "编码规则")
    @TableField("CODE_RULE_BO")
    private String codeRuleBo;
    /**
     * 标签
     */
    @ApiModelProperty(value = "标签")
    @TableField("LABEL_BO")
    private String labelBo;
    /**
     * 标签类型
     */
    @ApiModelProperty(value = "标签类型")
    @TableField("LABEL_TYPE")
    private String labelType;
    /**
     * 工厂
     */
    @ApiModelProperty(value = "工厂")
    @TableField("SITE")
    private String site;

    @ApiModelProperty(value = "建档日期")
    @TableField("CREATE_DATE")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "GMT+8"
    )
    private Date createDate;

    @ApiModelProperty(value = "建档人")
    @TableField("CREATE_USER")
    private String createUser;

    @ApiModelProperty(value = "修改日期")
    @TableField("MODIFY_DATE")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "GMT+8"
    )
    private Date modifyDate;

    @ApiModelProperty(value = "修改人")
    @TableField("MODIFY_USER")
    private String modifyUser;

    public void setObjectSetBasicAttribute( String userId,Date date ){
        this.createUser=userId;
        this.createDate=date;
        this.modifyUser=userId;
        this.modifyDate=date;
    }

}
