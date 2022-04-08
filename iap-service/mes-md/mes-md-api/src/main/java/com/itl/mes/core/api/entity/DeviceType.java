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
 * 设备类型表
 * </p>
 *
 * @author space
 * @since 2019-06-17
 */
@TableName("m_device_type")
@ApiModel(value="DeviceType",description="设备类型表")
public class DeviceType extends Model<DeviceType> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="RT:SITE,DEVICE_TYPE【PK】")
   @Length( max = 100 )
   @TableId(value = "BO", type = IdType.INPUT)
   private String bo;

   @ApiModelProperty(value="工厂【UK】")
   @Length( max = 32 )
   @TableField("SITE")
   private String site;

   @ApiModelProperty(value="编号【UK】")
   @Length( max = 64 )
   @TableField("DEVICE_TYPE")
   private String deviceType;

   @ApiModelProperty(value="名称")
   @Length( max = 100 )
   @TableField("DEVICE_TYPE_NAME")
   private String deviceTypeName;

   @ApiModelProperty(value="描述")
   @Length( max = 200 )
   @TableField("DEVICE_TYPE_DESC")
   private String deviceTypeDesc;

   @ApiModelProperty(value="建档人")
   @Length( max = 32 )
   @TableField("CREATE_USER")
   private String createUser;

   @ApiModelProperty(value="建档日期")
   @TableField("CREATE_DATE")
   @JsonFormat(
           pattern = "yyyy-MM-dd HH:mm:ss",
           timezone = "GMT+8"
   )
   private Date createDate;

   @ApiModelProperty(value="修改日期")
   @TableField("MODIFY_DATE")
   @JsonFormat(
           pattern = "yyyy-MM-dd HH:mm:ss",
           timezone = "GMT+8"
   )
   private Date modifyDate;

   @ApiModelProperty(value="修改人")
   @Length( max = 32 )
   @TableField("MODIFY_USER")
   private String modifyUser;


   public String getBo() {
      return bo;
   }

   public void setBo(String bo) {
      this.bo = bo;
   }

   public String getSite() {
      return site;
   }

   public void setSite(String site) {
      this.site = site;
   }

   public String getDeviceType() {
      return deviceType;
   }

   public void setDeviceType(String deviceType) {
      this.deviceType = deviceType;
   }

   public String getDeviceTypeName() {
      return deviceTypeName;
   }

   public void setDeviceTypeName(String deviceTypeName) {
      this.deviceTypeName = deviceTypeName;
   }

   public String getDeviceTypeDesc() {
      return deviceTypeDesc;
   }

   public void setDeviceTypeDesc(String deviceTypeDesc) {
      this.deviceTypeDesc = deviceTypeDesc;
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

   public Date getModifyDate() {
      return modifyDate;
   }

   public void setModifyDate(Date modifyDate) {
      this.modifyDate = modifyDate;
   }

   public String getModifyUser() {
      return modifyUser;
   }

   public void setModifyUser(String modifyUser) {
      this.modifyUser = modifyUser;
   }

   public static final String BO = "BO";

   public static final String SITE = "SITE";

   public static final String DEVICE_TYPE = "DEVICE_TYPE";

   public static final String DEVICE_TYPE_NAME = "DEVICE_TYPE_NAME";

   public static final String DEVICE_TYPE_DESC = "DEVICE_TYPE_DESC";

   public static final String CREATE_USER = "CREATE_USER";

   public static final String CREATE_DATE = "CREATE_DATE";

   public static final String MODIFY_DATE = "MODIFY_DATE";

   public static final String MODIFY_USER = "MODIFY_USER";

   @Override
   protected Serializable pkVal() {
      return this.bo;
   }


   public void setObjectSetBasicAttribute( String userId,Date date ){
     this.createUser=userId;
     this.createDate=date;
     this.modifyUser=userId;
     this.modifyDate=date;
   }

   @Override
   public String toString() {
      return "DeviceType{" +
         ", bo = " + bo +
         ", site = " + site +
         ", deviceType = " + deviceType +
         ", deviceTypeName = " + deviceTypeName +
         ", deviceTypeDesc = " + deviceTypeDesc +
         ", createUser = " + createUser +
         ", createDate = " + createDate +
         ", modifyDate = " + modifyDate +
         ", modifyUser = " + modifyUser +
         "}";
   }
}