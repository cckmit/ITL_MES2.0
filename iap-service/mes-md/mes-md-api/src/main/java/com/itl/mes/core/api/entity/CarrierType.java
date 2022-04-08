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
 * 载具类型表
 * </p>
 *
 * @author space
 * @since 2019-07-22
 */
@TableName("m_carrier_type")
@ApiModel(value="CarrierType",description="载具类型表")
public class CarrierType extends Model<CarrierType> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="CT:SITE,CARRIER_TYPE[PK-]")
   @Length( max = 100 )
   @TableId(value = "BO", type = IdType.INPUT)
   private String bo;

   @ApiModelProperty(value="工厂")
   @Length( max = 32 )
   @TableField("SITE")
   private String site;

   @ApiModelProperty(value="载具类型")
   @Length( max = 32 )
   @TableField("CARRIER_TYPE")
   private String carrierType;

   @ApiModelProperty(value="载具描述")
   @Length( max = 100 )
   @TableField("DESCRIPTION")
   private String description;

   @ApiModelProperty(value="容量")
   @TableField("CAPACITY")
   private Integer capacity;

   @ApiModelProperty(value="行数")
   @TableField("ROW_SIZE")
   private Integer rowSize;

   @ApiModelProperty(value="列数")
   @TableField("COLUMN_SIZE")
   private Integer columnSize;

   @ApiModelProperty(value="创建用户")
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

   @ApiModelProperty(value = "是否允许物料混装")
   @TableField("IS_BLEND")
   private String isBlend;


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

   public String getCarrierType() {
      return carrierType;
   }

   public void setCarrierType(String carrierType) {
      this.carrierType = carrierType;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public Integer getCapacity() {
      return capacity;
   }

   public void setCapacity(Integer capacity) {
      this.capacity = capacity;
   }

   public Integer getRowSize() {
      return rowSize;
   }

   public void setRowSize(Integer rowSize) {
      this.rowSize = rowSize;
   }

   public Integer getColumnSize() {
      return columnSize;
   }

   public void setColumnSize(Integer columnSize) {
      this.columnSize = columnSize;
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

   public String getIsBlend() { return isBlend; }

   public void setIsBlend(String isBlend) { this.isBlend = isBlend; }

   public static final String BO = "BO";

   public static final String SITE = "SITE";

   public static final String CARRIER_TYPE = "CARRIER_TYPE";

   public static final String DESCRIPTION = "DESCRIPTION";

   public static final String CAPACITY = "CAPACITY";

   public static final String ROW_SIZE = "ROW_SIZE";

   public static final String COLUMN_SIZE = "COLUMN_SIZE";

   public static final String CREATE_USER = "CREATE_USER";

   public static final String CREATE_DATE = "CREATE_DATE";

   public static final String MODIFY_USER = "MODIFY_USER";

   public static final String MODIFY_DATE = "MODIFY_DATE";

   public static final String IS_BLEND = "IS_BLEND";

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
      return "CarrierType{" +
         ", bo = " + bo +
         ", site = " + site +
         ", carrierType = " + carrierType +
         ", description = " + description +
         ", capacity = " + capacity +
         ", rowSize = " + rowSize +
         ", columnSize = " + columnSize +
         ", createUser = " + createUser +
         ", createDate = " + createDate +
         ", modifyUser = " + modifyUser +
         ", modifyDate = " + modifyDate +
         ", isBlend = " + isBlend +
         "}";
   }
}