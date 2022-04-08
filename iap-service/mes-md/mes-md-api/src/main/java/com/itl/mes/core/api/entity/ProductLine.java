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
 * 产线表
 * </p>
 *
 * @author space
 * @since 2019-05-24
 */
@TableName("m_product_line")
@ApiModel(value="ProductLine",description="产线表")
public class ProductLine extends Model<ProductLine> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="WC:SITE,PRODUCT_LINE【PK】")
   @Length( max = 100 )
   @TableId(value = "BO", type = IdType.INPUT)
   private String bo;

   @ApiModelProperty(value="工厂【UK】")
   @Length( max = 32 )
   @TableField("SITE")
   private String site;

   @ApiModelProperty(value="产线【UK】")
   @Length( max = 64 )
   @TableField("PRODUCT_LINE")
   private String productLine;

   @ApiModelProperty(value="产线描述")
   @Length( max = 200 )
   @TableField("PRODUCT_LINE_DESC")
   private String productLineDesc;

   @ApiModelProperty(value="所属车间 BO【UK】")
   @Length( max = 100 )
   @TableField("WORK_SHOP_BO")
   private String workShopBo;

   @ApiModelProperty(value="状态（已启用1，已禁用0）")
   @Length( max = 1 )
   @TableField("STATE")
   private String state;

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

   public String getProductLine() {
      return productLine;
   }

   public void setProductLine(String productLine) {
      this.productLine = productLine;
   }

   public String getProductLineDesc() {
      return productLineDesc;
   }

   public void setProductLineDesc(String productLineDesc) {
      this.productLineDesc = productLineDesc;
   }

   public String getWorkShopBo() {
      return workShopBo;
   }

   public void setWorkShopBo(String workShopBo) {
      this.workShopBo = workShopBo;
   }

   public String getState() {
      return state;
   }

   public void setState(String state) {
      this.state = state;
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

   public static final String PRODUCT_LINE = "PRODUCT_LINE";

   public static final String PRODUCT_LINE_DESC = "PRODUCT_LINE_DESC";

   public static final String WORK_SHOP_BO = "WORK_SHOP_BO";

   public static final String STATE = "STATE";

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
      return "ProductLine{" +
         ", bo = " + bo +
         ", site = " + site +
         ", productLine = " + productLine +
         ", productLineDesc = " + productLineDesc +
         ", workShopBo = " + workShopBo +
         ", state = " + state +
         ", createDate = " + createDate +
         ", createUser = " + createUser +
         ", modifyDate = " + modifyDate +
         ", modifyUser = " + modifyUser +
         "}";
   }
}