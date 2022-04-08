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
 * 载具表
 * </p>
 *
 * @author space
 * @since 2019-07-23
 */
@TableName("m_carrier")
@ApiModel(value="Carrier",description="载具表")
public class Carrier extends Model<Carrier> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="CARRIER:SITE,CARRIER[PK-]")
   @Length( max = 100 )
   @TableId(value = "BO", type = IdType.INPUT)
   private String bo;

   @ApiModelProperty(value="工厂")
   @Length( max = 32 )
   @TableField("SITE")
   private String site;

   @ApiModelProperty(value="载具")
   @Length( max = 32 )
   @TableField("CARRIER")
   private String carrier;

   @ApiModelProperty(value="描述")
   @Length( max = 100 )
   @TableField("DESCRIPTION")
   private String description;

   @ApiModelProperty(value="载具类型")
   @Length( max = 100 )
   @TableField("CARRIER_TYPE_BO")
   private String carrierTypeBo;

   @ApiModelProperty(value="使用次数")
   @TableField("USE_COUNT")
   private Integer useCount;

   @ApiModelProperty(value="创建人")
   @Length( max = 32 )
   @TableField("CREATE_USER")
   private String createUser;

   @ApiModelProperty(value="创建时间")
   @TableField("CREATE_DATE")
   private Date createDate;

   @ApiModelProperty(value="修改人")
   @Length( max = 32 )
   @TableField("MODIFY_USER")
   private String modifyUser;

   @ApiModelProperty(value="修改时间")
   @TableField("MODIFY_DATE")
   private Date modifyDate;


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

   public String getCarrier() {
      return carrier;
   }

   public void setCarrier(String carrier) {
      this.carrier = carrier;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getCarrierTypeBo() {
      return carrierTypeBo;
   }

   public void setCarrierTypeBo(String carrierTypeBo) {
      this.carrierTypeBo = carrierTypeBo;
   }

   public Integer getUseCount() {
      return useCount;
   }

   public void setUseCount(Integer useCount) {
      this.useCount = useCount;
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

   public static final String BO = "BO";

   public static final String SITE = "SITE";

   public static final String CARRIER = "CARRIER";

   public static final String DESCRIPTION = "DESCRIPTION";

   public static final String CARRIER_TYPE_BO = "CARRIER_TYPE_BO";

   public static final String USE_COUNT = "USE_COUNT";

   public static final String CREATE_USER = "CREATE_USER";

   public static final String CREATE_DATE = "CREATE_DATE";

   public static final String MODIFY_USER = "MODIFY_USER";

   public static final String MODIFY_DATE = "MODIFY_DATE";

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
      return "Carrier{" +
         ", bo = " + bo +
         ", site = " + site +
         ", carrier = " + carrier +
         ", descirption = " + description +
         ", carrierTypeBo = " + carrierTypeBo +
         ", useCount = " + useCount +
         ", createUser = " + createUser +
         ", createDate = " + createDate +
         ", modifyUser = " + modifyUser +
         ", modifyDate = " + modifyDate +
         "}";
   }
}