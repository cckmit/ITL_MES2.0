package com.itl.mes.andon.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 安灯等级推送
 *
 * @author cuichonghe
 * @date 2020-12-14 14:56:55
 */
@Data
@TableName("andon_grade_push")
@ApiModel(value = "grade_push", description = "安灯等级推送")
public class GradePush implements Serializable {
  private static final long serialVersionUID = 1L;

  /** ID */
  @ApiModelProperty(value = "ID")
  @TableId(value = "ID", type = IdType.AUTO)
  private int id;
  /** BO */
  @ApiModelProperty(value = "BO")
  @TableField(value = "BO")
  private String BO;
  /** 工厂 */
  @ApiModelProperty(value = "工厂")
  private String site;
  /** 安灯等级 */
  @ApiModelProperty(value = "安灯等级")
  @TableField("ANDON_GRADE_ID")
  private String andonGradeID;
  /** 安灯等级 */
  @ApiModelProperty(value = "呼叫类型ID")
  @TableField("CALL_TYPE_ID")
  private String callTypeID;
  /** 推送人员(关联员工) */
  @ApiModelProperty(value = "推送人员")
  @TableField("PUSH_TO_USER")
  private String pushToUser;
  /** 推送职级(关联岗位) */
  @ApiModelProperty(value = "推送职级")
  @TableField("PUSH_TO_POSITION")
  private String pushToPosition;
  /** 推送职级(关联岗位) */
  @ApiModelProperty(value = "推送人员电话")
  @TableField("USER_MOBILE")
  private String userMobile;
  /** 推送职级(关联岗位) */
  @ApiModelProperty(value = "推送人员邮箱")
  @TableField("USER_EMAIL")
  private String userEmail;
  /** 推送职级(关联岗位) */
  @ApiModelProperty(value = "推送人员姓名")
  @TableField("REAL_NAME")
  private String realName;


  /*@TableField(exist = false)
  @ApiModelProperty(value = "安灯等级编号")
  private String andonGrade;

  @TableField(exist = false)
  @ApiModelProperty(value = "安灯名称")
  private String andonGradeName;

  @ApiModelProperty(value = "安灯描述")
  @TableField(exist = false)
  private String andonGradeDesc;

  @ApiModelProperty(value = "安灯描述")
  @TableField(exist = false)
  private String state;*/
}
