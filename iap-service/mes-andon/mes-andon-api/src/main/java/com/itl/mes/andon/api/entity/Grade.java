package com.itl.mes.andon.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.mes.andon.api.constant.CustomCommonConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

/**
 * 安灯等级
 *
 * @author cuichonghe
 * @date 2020-12-14 14:56:55
 */
@Data
@TableName("andon_grade")
@ApiModel(value = "Grade", description = "安灯等级表")
public class Grade implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "id")
    @TableId(value = "ID", type = IdType.AUTO)
    private int id;

    @ApiModelProperty(value = "ANDON_GRADE:SITE,ANDON_GRADE【PK】")
    @Length(max = 100)
    @TableField(value = "BO")
    private String bo;

    @ApiModelProperty(value = "工厂")
    @Length(max = 100)
    @TableField("SITE")
    private String site;

    @ApiModelProperty(value = "安灯预警级别")
    @Length(max = 64)
    @TableField("ANDON_GRADE")
    private int andonGrade;

    @ApiModelProperty(value = "状态（已启用0，已禁用1）")
    @Length(max = 1)
    @TableField("STATE")
    private String state;

    @ApiModelProperty(value = "建档日期")
    @TableField("CREATE_DATE")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date createDate;

    @ApiModelProperty(value = "建档人")
    @TableField("CREATE_USER")
    private String createUser;

    @ApiModelProperty(value = "修改日期")
    @TableField("MODIFY_DATE")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date modifyDate;

    @ApiModelProperty(value = "修改人")
    @TableField("MODIFY_USER")
    private String modifyUser;

    @ApiModelProperty(value = "关联车间")
    @TableField("WORKSHOP_BO")
    private String workshopBo;

    @ApiModelProperty(value = "关联车间")
    @TableField("WORKSHOP_NAME")
    private String workshopName;

    @ApiModelProperty(value = "自动超时升级时间")
    @TableField("OVER_TIME")
    private Integer overTime;

    @ApiModelProperty(value = "超时类别")
    @TableField("TIMEOUT_TYPE")
    private Integer timeoutType;

    @ApiModelProperty(value = "安灯类型名称")
    @TableField("ANDON_TYPE_NAME")
    private String andonTypeName;

    @ApiModelProperty(value = "安灯类型编号")
    @TableField("ANDON_TYPE")
    private String andonType;
}
