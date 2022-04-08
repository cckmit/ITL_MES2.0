package com.itl.mes.me.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yaoxiang
 * @date 2020/12/25
 * @since JDK1.8
 */
@Data
@ApiModel("作业指导书内容保存dto")
public class InstructorItemDto {
    @ApiModelProperty(value = "bo")
    private String bo;
    /**
     * 指导书编号
     */
    @ApiModelProperty(value = "指导书BO")
    private String instructorBo;

    /**
     * 内容项编号
     */
    @ApiModelProperty(value = "内容项编号")
    private String instructorItem;
    /**
     * 内容项名称
     */
    @ApiModelProperty(value = "内容项名称")
    private String instructorItemName;
    /**
     * 是否启用
     */
    @ApiModelProperty(value = "是否启用")
    private Integer state;
    /**
     * 颜色
     */
    @ApiModelProperty(value = "颜色")
    private String color;
    /**
     * 是否默认显示
     */
    @ApiModelProperty(value = "是否默认显示")
    private Integer defaultShow;
}
