package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 工位类型表
 * </p>
 *
 * @author space
 * @since 2019-05-31
 */
@TableName("m_station_type")
@ApiModel(value="StationType",description="工位类型表")
public class StationType extends Model<StationType> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="ST:SITE,STATION_TYPE【PK】")
   @Length( max = 200 )
   @TableId(value = "BO", type = IdType.INPUT)
   private String bo;

   @ApiModelProperty(value="工厂【UK】")
   @Length( max = 32 )
   @TableField("SITE")
   private String site;

   @ApiModelProperty(value="编号【UK】")
   @Length( max = 64 )
   @TableField("STATION_TYPE")
   private String stationType;

   @ApiModelProperty(value="名称")
   @Length( max = 64 )
   @TableField("STATION_TYPE_NAME")
   private String stationTypeName;

   @ApiModelProperty(value="描述")
   @Length( max = 200 )
   @TableField("STATION_TYPE_DESC")
   private String stationTypeDesc;

   @ApiModelProperty(value="建档日期")
   @TableField("CREATE_DATE")
   private Date createDate;

   @ApiModelProperty(value="建档人")
   @Length( max = 32 )
   @TableField("CREATE_USER")
   private String createUser;

   @ApiModelProperty(value="修改日期")
   @TableField("MODIFY_DATE")
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

   public String getStationType() {
      return stationType;
   }

   public void setStationType(String stationType) {
      this.stationType = stationType;
   }

   public String getStationTypeName() {
      return stationTypeName;
   }

   public void setStationTypeName(String stationTypeName) {
      this.stationTypeName = stationTypeName;
   }

   public String getStationTypeDesc() {
      return stationTypeDesc;
   }

   public void setStationTypeDesc(String stationTypeDesc) {
      this.stationTypeDesc = stationTypeDesc;
   }

   public Date getCreateDate() {
      return createDate;
   }

   public void setCreateDate(Date createDate) {
      this.createDate = createDate;
   }

   public String getCreateUser() {
      return createUser;
   }

   public void setCreateUser(String createUser) {
      this.createUser = createUser;
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

   public static final String STATION_TYPE = "STATION_TYPE";

   public static final String STATION_TYPE_NAME = "STATION_TYPE_NAME";

   public static final String STATION_TYPE_DESC = "STATION_TYPE_DESC";

   public static final String CREATE_DATE = "CREATE_DATE";

   public static final String CREATE_USER = "CREATE_USER";

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
      return "StationType{" +
         ", bo = " + bo +
         ", site = " + site +
         ", stationType = " + stationType +
         ", stationTypeName = " + stationTypeName +
         ", stationTypeDesc = " + stationTypeDesc +
         ", createDate = " + createDate +
         ", createUser = " + createUser +
         ", modifyDate = " + modifyDate +
         ", modifyUser = " + modifyUser +
         "}";
   }
}