package com.itl.mes.andon.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.mes.andon.api.constant.CustomCommonConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

/**
 * 安灯类型
 *
 * @author cuichonghe
 * @date 2020-12-14 14:56:55
 */
@Data
@TableName("andon_type")
@ApiModel(value = "type", description = "安灯类型")
@Accessors(chain = true)
public class Type implements Serializable {
  private static final long serialVersionUID = 1L;

  /** ANDON_TYPE:SITE,ANDON_TYPE【PK】 */
  @TableId(value = "BO", type = IdType.INPUT)
  @Length(max = 100)
  @ApiModelProperty(value = "ANDON_TYPE:SITE,ANDON_TYPE【PK】")
  private String bo;
  /** 工厂 */
  @TableField("SITE")
  @Length(max = 100)
  @ApiModelProperty(value = "工厂")
  private String site;
  /** 安灯类型 */
  @TableField("ANDON_TYPE")
  @Length(max = 64)
  @ApiModelProperty(value = "安灯类型")
  private String andonType;
  /** 名称 */
  @TableField("ANDON_TYPE_NAME")
  @Length(max = 64)
  @ApiModelProperty(value = "名称")
  private String andonTypeName;
  /** 描述 */
  @TableField("ANDON_TYPE_DESC")
  @Length(max = 256)
  @ApiModelProperty(value = "描述")
  private String andonTypeDesc;
  /** 安灯推送 */
  @TableField("ANDON_PUSH_BO")
  @Length(max = 100)
  @ApiModelProperty(value = "安灯推送")
  private String andonPushBo;
  /** 状态（已启用1，已禁用0） */
  @TableField("STATE")
  @Length(max = 1)
  @ApiModelProperty(value = "状态（已启用1，已禁用0）")
  private String state;

  @TableField("ANDON_TYPE_TAG")
  @ApiModelProperty(value = "类型标识")
  private String andonTypeTag;

  /** 建档日期 */
  @TableField("CREATE_DATE")
  @ApiModelProperty(value = "建档日期")
  @JsonFormat(
          pattern = "yyyy-MM-dd HH:mm:ss",
          timezone = "GMT+8"
  )
  private Date createDate;
  /** 建档人 */
  @TableField("CREATE_USER")
  @ApiModelProperty(value = "建档人")
  private String createUser;
  /** 修改日期 */
  @TableField("MODIFY_DATE")
  @ApiModelProperty(value = "修改日期")
  @JsonFormat(
          pattern = "yyyy-MM-dd HH:mm:ss",
          timezone = "GMT+8"
  )
  private Date modifyDate;
  /** 修改人 */
  @TableField("MODIFY_USER")
  @ApiModelProperty(value = "修改人")
  private String modifyUser;
  /** 修改人 */
  @TableField("ABNORMAL_TYPE")
  @ApiModelProperty(value = "异常类型")
  private int abnormalType;

  /** 是否校验 */
  @TableField("isCheck")
  @ApiModelProperty(value = "是否校验")
  private String isCheck;

  public void setObjectSetBasicAttribute( String userId, Date date ){
    this.createUser=userId;
    this.createDate=date;
    this.modifyUser=userId;
    this.modifyDate=date;
  }

  public static final String BO = "BO";

  public static final String ANDON_TYPE = "ANDON_TYPE";

  public static final String ANDON_TYPE_NAME = "ANDON_TYPE_NAME";

  public static final String ANDON_TYPE_DESC = "ANDON_TYPE_DESC";

  public static final String STATE = "STATE";

  public static final String SITE = "SITE";

  public static final String ANDON_PUSH_BO = "ANDON_PUSH_BO";

  public static final String CREATE_DATE = "CREATE_DATE";

  public static final String CREATE_USER = "CREATE_USER";

  public static final String MODIFY_DATE = "MODIFY_DATE";

  public static final String MODIFY_USER = "MODIFY_USER";

  public static final String ANDON_TYPE_TAG = "ANDON_TYPE_TAG";
}
