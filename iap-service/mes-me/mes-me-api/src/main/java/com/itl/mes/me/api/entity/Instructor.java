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
 * 作业指导书
 *
 * @author yaoxiang
 * @date 2020-12-25
 */
@Data
@Accessors(chain = true)
@TableName("m_instructor")
@ApiModel(value = "m_instructor", description = "作业指导书")
public class Instructor implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * INSTRUCTOR:SITE,NAME,VERSION PK
     */
    @TableId(value = "BO", type = IdType.INPUT)
    @ApiModelProperty(value = "INSTRUCTOR:SITE,NAME,VERSION PK")
    private String bo;
    /**
     * 工厂
     */
    @ApiModelProperty(value = "工厂")
    @TableField("SITE")
    private String site;
    /**
     * 指导书编号
     */
    @ApiModelProperty(value = "指导书编号")
    @TableField("INSTRUCTOR")
    private String instructor;
    /**
     * 指导书URL
     */
    @ApiModelProperty(value = "指导书URL")
    @TableField("INSTRUCTOR_FILE")
    private String instructorFile;
    /**
     * 指导书名称
     */
    @ApiModelProperty(value = "指导书名称")
    @TableField("INSTRUCTOR_NAME")
    private String instructorName;
    /**
     * 指导书模块
     */
    @ApiModelProperty(value = "指导书描述")
    @TableField("INSTRUCTOR_DESC")
    private String instructorDesc;
    /**
     * 版本
     */
    @ApiModelProperty(value = "版本")
    @TableField("VERSION")
    private String version;
    /**
     * 说明
     */
    @ApiModelProperty(value = "说明")
    @TableField("EXPLAIN")
    private String explain;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    @TableField("STATE")
    private String state;
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
