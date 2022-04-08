package com.itl.mes.me.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author yaoxiang
 * @date 2020/12/25
 * @since JDK1.8
 */
@Data
@ApiModel("作业指导书保存dto")
public class InstructorSaveDto {
    @ApiModelProperty(value = "bo")
    private String bo;
    /**
     * 指导书编号
     */
    @ApiModelProperty(value = "指导书编号")
    private String instructor;
    /**
     * 指导书URL
     */
    @ApiModelProperty(value = "指导书URL")
    private String instructorFile;
    /**
     * 指导书名称
     */
    @ApiModelProperty(value = "指导书名称")
    private String instructorName;
    /**
     * 指导书模块
     */
    @ApiModelProperty(value = "指导书描述")
    private String instructorDesc;
    /**
     * 版本
     */
    @ApiModelProperty(value = "版本")
    private String version;
    /**
     * 说明
     */
    @ApiModelProperty(value = "说明")
    private String explain;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private String state;
}
