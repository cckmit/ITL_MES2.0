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
 * 线边仓信息表
 * </p>
 *
 * @author space
 * @since 2019-07-17
 */
@TableName("w_warehouse")
@ApiModel(value="Warehouse",description="线边仓信息表")
public class Warehouse extends Model<Warehouse> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="WAREHOUSE:SITE,VARHOUSE【PK】")
   @Length( max = 100 )
   @TableId(value = "BO", type = IdType.INPUT)
   private String bo;

   @ApiModelProperty(value="工厂【UK-】")
   @Length( max = 32 )
   @TableField("SITE")
   private String site;

   @ApiModelProperty(value="线边仓编号【UK-】")
   @Length( max = 64 )
   @TableField("WAREHOUSE")
   private String warehouse;

   @ApiModelProperty(value="线边仓名称")
   @Length( max = 100 )
   @TableField("WAREHOUSE_NAME")
   private String warehouseName;

   @ApiModelProperty(value="线边仓描述")
   @Length( max = 200 )
   @TableField("WAREHOUSE_DESC")
   private String warehouseDesc;

   @ApiModelProperty(value="线边仓类别")
   @Length( max = 32 )
   @TableField("WAREHOUSE_TYPE")
   private String warehouseType;

   @ApiModelProperty(value="建档人")
   @Length( max = 32 )
   @TableField("CREATE_USER")
   private String createUser;

   @ApiModelProperty(value="建档时间")
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

   public String getWarehouse() {
      return warehouse;
   }

   public void setWarehouse(String warehouse) {
      this.warehouse = warehouse;
   }

   public String getWarehouseName() {
      return warehouseName;
   }

   public void setWarehouseName(String warehouseName) {
      this.warehouseName = warehouseName;
   }

   public String getWarehouseDesc() {
      return warehouseDesc;
   }

   public void setWarehouseDesc(String warehouseDesc) {
      this.warehouseDesc = warehouseDesc;
   }

   public String getWarehouseType() {
      return warehouseType;
   }

   public void setWarehouseType(String warehouseType) {
      this.warehouseType = warehouseType;
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

   public static final String WAREHOUSE = "WAREHOUSE";

   public static final String WAREHOUSE_NAME = "WAREHOUSE_NAME";

   public static final String WAREHOUSE_DESC = "WAREHOUSE_DESC";

   public static final String WAREHOUSE_TYPE = "WAREHOUSE_TYPE";

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
      return "Warehouse{" +
         ", bo = " + bo +
         ", site = " + site +
         ", warehouse = " + warehouse +
         ", warehouseName = " + warehouseName +
         ", warehouseDesc = " + warehouseDesc +
         ", warehouseType = " + warehouseType +
         ", createUser = " + createUser +
         ", createDate = " + createDate +
         ", modifyUser = " + modifyUser +
         ", modifyDate = " + modifyDate +
         "}";
   }
}