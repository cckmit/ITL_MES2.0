package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 车间表
 * </p>
 *
 * @author space
 * @since 2019-06-06
 */
@TableName("m_work_shop")
@ApiModel(value="WorkShop",description="车间表")
public class WorkShop extends Model<WorkShop> {

   private static final long serialVersionUID = 1L;

   @ApiModelProperty(value="主键")
   @NotBlank
   @Length( max = 100 )
   @TableId(value = "BO", type = IdType.INPUT)
   private String bo;

   @ApiModelProperty(value="工厂")
   @NotBlank
   @Length( max = 32 )
   @TableField("SITE")
   private String site;

   @ApiModelProperty(value="车间")
   @NotBlank
   @Length( max = 64 )
   @TableField("WORK_SHOP")
   private String workShop;

   @ApiModelProperty(value="车间描述")
   @Length( max = 200 )
   @TableField("WORK_SHOP_DESC")
   private String workShopDesc;

   @ApiModelProperty(value="状态（已启用1，已禁用0）")
   @Length( max = 1 )
   @TableField("STATE")
   private String state;

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

   public String getWorkShop() {
      return workShop;
   }

   public void setWorkShop(String workShop) {
      this.workShop = workShop;
   }

   public String getWorkShopDesc() {
      return workShopDesc;
   }

   public void setWorkShopDesc(String workShopDesc) {
      this.workShopDesc = workShopDesc;
   }

   public String getState() {
      return state;
   }

   public void setState(String state) {
      this.state = state;
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

   public static final String WORK_SHOP = "WORK_SHOP";

   public static final String WORK_SHOP_DESC = "WORK_SHOP_DESC";

   public static final String STATE = "STATE";

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
      return "WorkShop{" +
              ", bo = " + bo +
              ", site = " + site +
              ", workShop = " + workShop +
              ", workShopDesc = " + workShopDesc +
              ", state = " + state +
              ", createUser = " + createUser +
              ", createDate = " + createDate +
              ", modifyUser = " + modifyUser +
              ", modifyDate = " + modifyDate +
              "}";
   }
}