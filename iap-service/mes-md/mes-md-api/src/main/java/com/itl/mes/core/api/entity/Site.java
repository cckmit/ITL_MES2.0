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
 * 工厂表
 * </p>
 *
 * @author space
 * @since 2019-05-23
 */
@TableName("m_site")
@ApiModel(value="Site",description="工厂表")
public class Site extends Model<Site> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="主键")
   @Length( max = 64 )
   @TableId(value = "SITE", type = IdType.INPUT)
   private String site;

   @ApiModelProperty(value="描述")
   @Length( max = 50 )
   @TableField("SITE_DESC")
   private String siteDesc;

   @ApiModelProperty(value="类型")
   @Length( max = 32 )
   @TableField("SITE_TYPE")
   private String siteType;

   @ApiModelProperty(value="是否启用标记(缺省为1)")
   @Length( max = 1 )
   @TableField("ENABLE_FLAG")
   private String enableFlag;

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


   public String getSite() {
      return site;
   }

   public void setSite(String site) {
      this.site = site;
   }

   public String getSiteDesc() {
      return siteDesc;
   }

   public void setSiteDesc(String siteDesc) {
      this.siteDesc = siteDesc;
   }

   public String getSiteType() {
      return siteType;
   }

   public void setSiteType(String siteType) {
      this.siteType = siteType;
   }

   public String getEnableFlag() {
      return enableFlag;
   }

   public void setEnableFlag(String enableFlag) {
      this.enableFlag = enableFlag;
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

   public static final String SITE = "SITE";

   public static final String SITE_DESC = "SITE_DESC";

   public static final String SITE_TYPE = "SITE_TYPE";

   public static final String ENABLE_FLAG = "ENABLE_FLAG";

   public static final String CREATE_USER = "CREATE_USER";

   public static final String CREATE_DATE = "CREATE_DATE";

   public static final String MODIFY_USER = "MODIFY_USER";

   public static final String MODIFY_DATE = "MODIFY_DATE";

   @Override
   protected Serializable pkVal() {
      return this.site;
   }


   public void setObjectSetBasicAttribute( String userId,Date date ){
      this.createUser=userId;
      this.createDate=date;
      this.modifyUser=userId;
      this.modifyDate=date;
   }

   @Override
   public String toString() {
      return "Site{" +
              ", site = " + site +
              ", siteDesc = " + siteDesc +
              ", siteType = " + siteType +
              ", enableFlag = " + enableFlag +
              ", createUser = " + createUser +
              ", createDate = " + createDate +
              ", modifyUser = " + modifyUser +
              ", modifyDate = " + modifyDate +
              "}";
   }
}