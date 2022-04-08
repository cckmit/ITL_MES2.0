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

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 安灯推送设置
 *
 * @author cuichonghe
 * @date 2020-12-14 14:56:55
 */
@Data
@TableName("andon_push")
@ApiModel(value = "push", description = "安灯推送设置")
public class Push implements Serializable {
  private static final long serialVersionUID = 1L;

  /** ANDON_PUSH:SITE,ANDON_PUSH【PK】 */
  @TableId(value = "bo", type = IdType.INPUT)
  @ApiModelProperty(value = "ANDON_PUSH:SITE,ANDON_PUSH【PK】")
  private String bo;
  /** 工厂 */
  @ApiModelProperty(value = "工厂")
  private String site;
  /** 安灯推送 */
  @ApiModelProperty(value = "安灯推送")
  @TableField("ANDON_PUSH")
  private String andonPush;
  /** 名称 */
  @ApiModelProperty(value = "名称")
  @TableField("ANDON_PUSH_NAME")
  private String andonPushName;
  /** 描述 */
  @ApiModelProperty(value = "描述")
  @TableField("ANDON_PUSH_DESC")
  private String andonPushDesc;
  /** 状态（已启用1，已禁用0） */
  @ApiModelProperty(value = "状态（已启用1，已禁用0）")
  private String state;
  /** 建档日期 */
  @ApiModelProperty(value = "建档日期")
  @JsonFormat(
          pattern = "yyyy-MM-dd HH:mm:ss",
          timezone = "GMT+8"
  )
  @TableField("CREATE_DATE")
  private Date createDate;
  /** 建档人 */
  @ApiModelProperty(value = "建档人")
  @TableField("CREATE_USER")
  private String createUser;
  /** 修改日期 */
  @ApiModelProperty(value = "修改日期")
  @JsonFormat(
          pattern = "yyyy-MM-dd HH:mm:ss",
          timezone = "GMT+8"
  )  @TableField("MODIFY_DATE")
  private Date modifyDate;
  /** 修改人 */
  @ApiModelProperty(value = "修改人")
  @TableField("MODIFY_USER")
  private String modifyUser;


  @TableField(exist = false)
  private List<GradePush> grades;
}
