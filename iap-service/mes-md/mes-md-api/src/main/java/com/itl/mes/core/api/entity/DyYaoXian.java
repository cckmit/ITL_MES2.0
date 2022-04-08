package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@TableName("m_dy_yaoxian")
@ApiModel(value="DyYaoXian",description="摇线表")
@Data
public class DyYaoXian extends Model<DyYaoXian> {
    /**
     * uuid，唯一标识
     */
    @ApiModelProperty(value="UUID")
    @TableId(value = "ID", type = IdType.INPUT)
    private String id;
    /**
     * 物料编码
     */
    @ApiModelProperty(value="物料编码")
    @TableField("ITEM")
    private String item;
    /**
     * 物料版本
     */
    @ApiModelProperty(value="物料版本 ")
    @TableField("VERSION")
    private String version;
    /**
     * 物料名称
     */
    @ApiModelProperty(value="物料名称")
    @TableField("ITEM_NAME")
    private String itemName;
    /**
     * 物料描述
     */
    @ApiModelProperty(value="物料描述")
    @TableField("ITEM_DESC")
    private String itemDesc;
    /**
     * 主线线径
     */
    @ApiModelProperty(value="主线线径")
    @TableField("MAIN_DIAMETER")
    private String mainDiameter;
    /**
     * 主线绕组一
     */
    @ApiModelProperty(value="主线绕组一")
    @TableField("MAIN_CIRCLE1")
    private String mainCircle1;
    /**
     * 主线绕组二
     */
    @ApiModelProperty(value="主线绕组二")
    @TableField("MAIN_CIRCLE2")
    private String mainCircle2;
    /**
     * 主线绕组三
     */
    @ApiModelProperty(value="主线绕组三")
    @TableField("MAIN_CIRCLE3")
    private String mainCircle3;
    /**
     * 主线绕组四
     */
    @ApiModelProperty(value="主线绕组四")
    @TableField("MAIN_CIRCLE4")
    private String mainCircle4;
    /**
     * 主线绕组五
     */
    @ApiModelProperty(value="主线绕组五")
    @TableField("MAIN_CIRCLE5")
    private String mainCircle5;
    /**
     * 副线线径
     */
    @ApiModelProperty(value="副线线径")
    @TableField("AUXILIARY_DIAMETER")
    private String auxiliaryDiameter;
    /**
     * 副线绕组一
     */
    @ApiModelProperty(value="副线绕组一")
    @TableField("AUXILIARY_CIRCLE1")
    private String auxiliaryCircle1;
    /**
     * 副线绕组二
     */
    @ApiModelProperty(value="副线绕组二")
    @TableField("AUXILIARY_CIRCLE2")
    private String auxiliaryCircle2;
    /**
     * 副线绕组三
     */
    @ApiModelProperty(value="副线绕组三")
    @TableField("AUXILIARY_CIRCLE3")
    private String auxiliaryCircle3;
    /**
     * 副线绕组四
     */
    @ApiModelProperty(value="副线绕组四")
    @TableField("AUXILIARY_CIRCLE4")
    private String auxiliaryCircle4;
    /**
     * 副线绕组五
     */
    @ApiModelProperty(value="副线绕组一五")
    @TableField("AUXILIARY_CIRCLE5")
    private String auxiliaryCircle5;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
