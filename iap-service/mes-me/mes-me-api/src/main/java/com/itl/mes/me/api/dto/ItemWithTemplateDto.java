package com.itl.mes.me.api.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yaoxiang
 * @date 2020/12/28
 * @since JDK1.8
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "带模板的ItemDto")
public class ItemWithTemplateDto implements Serializable {
    @ApiModelProperty(value = "INSTRUCTOR_ITEM:SITE,INSTRUCTORITEM,VERSION【PK】")
    private String bo;
    @ApiModelProperty(value = "工厂")
    private String site;
    @ApiModelProperty(value = "指导书编号")
    private String instructorBo;
    @ApiModelProperty(value = "内容项编号")
    private String instructorItem;
    @ApiModelProperty(value = "内容项名称")
    private String instructorItemName;
    @ApiModelProperty(value = "是否启用")
    private Integer state;
    @ApiModelProperty(value = "颜色")
    private String color;
    @ApiModelProperty(value = "是否默认显示")
    private Integer defaultShow;
    @ApiModelProperty(value = "模板")
    private String template;
}
