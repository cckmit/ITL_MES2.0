package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * <p>
 * 班组成员表
 * </p>
 *
 * @author space
 * @since 2019-06-25
 */
@TableName("m_team_member")
@ApiModel(value="TeamMember",description="班组成员表")
public class TeamMember extends Model<TeamMember> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="班组，M_SHIFT_INFO表的BO【FK-】")
   @Length( max = 100 )
   @TableField("TEAM_BO")
   private String teamBo;

   @ApiModelProperty(value="班组成员")
   @Length( max = 100 )
   @TableField("USER_BO")
   private String userBo;


   public String getTeamBo() {
      return teamBo;
   }

   public void setTeamBo(String teamBo) {
      this.teamBo = teamBo;
   }

   public String getUserBo() {
      return userBo;
   }

   public void setUserBo(String userBo) {
      this.userBo = userBo;
   }

   public static final String TEAM_BO = "TEAM_BO";

   public static final String USER_BO = "USER_BO";



   @Override
   public String toString() {
      return "TeamMember{" +
         ", teamBo = " + teamBo +
         ", userBo = " + userBo +
         "}";
   }

   @Override
   protected Serializable pkVal() {
      return null;
   }
}