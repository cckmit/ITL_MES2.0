package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 工厂用户关系维护
 * </p>
 *
 * @author space
 * @since 2019-07-18
 */
@TableName("m_site_usr")
@ApiModel(value="SiteUsr",description="工厂用户关系维护")
public class SiteUsr extends Model<SiteUsr> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="用户")
   @Length( max = 32 )
   @TableId(value = "USR", type = IdType.INPUT)
   private String usr;

   @ApiModelProperty(value="工厂")
   @Length( max = 64 )
   @TableField("SITE")
   private String site;

   @ApiModelProperty(value="是否默认工厂(Y/N)")
   @Length( max = 1 )
   @TableField("IS_DEFAULT_SITE")
   private String isDefaultSite;

   @ApiModelProperty(value="建档人")
   @Length( max = 32 )
   @TableField("CREATE_USER")
   private String createUser;

   @ApiModelProperty(value="建档日期")
   @TableField("CREATE_DATE")
   private Date createDate;

   @ApiModelProperty(value="修改人")
   @Length( max = 32 )
   @TableField("MODIFY_USER")
   private String modifyUser;

   @ApiModelProperty(value="修改日期")
   @TableField("MODIFY_DATE")
   @JsonFormat(
           pattern = "yyyy-MM-dd HH:mm:ss.SSS",
           timezone = "GMT+8"
   )
   private Date modifyDate;


   public String getUsr() {
      return usr;
   }

   public void setUsr(String usr) {
      this.usr = usr;
   }

   public String getSite() {
      return site;
   }

   public void setSite(String site) {
      this.site = site;
   }

   public String getIsDefaultSite() {
      return isDefaultSite;
   }

   public void setIsDefaultSite(String isDefaultSite) {
      this.isDefaultSite = isDefaultSite;
   }

   public String getCreateUser() {
      return createUser;
   }

   public void setCreateUser(String createUser) {
      this.createUser = createUser;
   }

   public Date getCreateDate() {
      return createDate;
   }

   public void setCreateDate(Date createDate) {
      this.createDate = createDate;
   }

   public String getModifyUser() {
      return modifyUser;
   }

   public void setModifyUser(String modifyUser) {
      this.modifyUser = modifyUser;
   }

   public Date getModifyDate() {
      return modifyDate;
   }

   public void setModifyDate(Date modifyDate) {
      this.modifyDate = modifyDate;
   }

   public static final String USR = "USR";

   public static final String SITE = "SITE";

   public static final String IS_DEFAULT_SITE = "IS_DEFAULT_SITE";

   public static final String CREATE_USER = "CREATE_USER";

   public static final String CREATE_DATE = "CREATE_DATE";

   public static final String MODIFY_USER = "MODIFY_USER";

   public static final String MODIFY_DATE = "MODIFY_DATE";

   @Override
   protected Serializable pkVal() {
      return this.usr+","+this.site;
   }


   public void setObjectSetBasicAttribute( String userId,Date date ){
      this.createUser=userId;
      this.createDate=date;
      this.modifyUser=userId;
      this.modifyDate=date;
   }

   @Override
   public String toString() {
      return "SiteUsr{" +
              ", usr = " + usr +
              ", site = " + site +
              ", isDefaultSite = " + isDefaultSite +
              ", createUser = " + createUser +
              ", createDate = " + createDate +
              ", modifyUser = " + modifyUser +
              ", modifyDate = " + modifyDate +
              "}";
   }
}