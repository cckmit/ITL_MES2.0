package com.itl.mes.core.api.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 物料组表
 * </p>
 *
 * @author space
 * @since 2019-05-31
 */
@TableName("m_item_group")
@ApiModel(value="ItemGroup",description="物料组表")
public class ItemGroup extends Model<ItemGroup> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="IG:SITE,ITEM_GROUP[PK]")
   @Length( max = 100 )
   @NotBlank
   @TableId(value = "BO", type = IdType.INPUT)
   private String bo;

   @ApiModelProperty(value="工厂")
   @Length( max = 32 )
   @NotBlank
   @TableField("SITE")
   @Excel( name="工厂", orderNum="0" )
   private String site;

   @ApiModelProperty(value="物料组编号")
   @Length( max = 64 )
   @NotBlank
   @TableField("ITEM_GROUP")
   @Excel( name="物料组编号", orderNum="1" )
   private String itemGroup;

   @ApiModelProperty(value="物料组名称")
   @Length( max = 50 )
   @TableField("GROUP_NAME")
   @Excel( name="物料组名称", orderNum="2" )
   private String groupName;

   @ApiModelProperty(value="物料组描述")
   @Length( max = 200 )
   @TableField("GROUP_DESC")
   @Excel( name="物料组描述", orderNum="3" )
   private String groupDesc;

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

   public String getItemGroup() {
      return itemGroup;
   }

   public void setItemGroup(String itemGroup) {
      this.itemGroup = itemGroup;
   }

   public String getGroupName() {
      return groupName;
   }

   public void setGroupName(String groupName) {
      this.groupName = groupName;
   }

   public String getGroupDesc() {
      return groupDesc;
   }

   public void setGroupDesc(String groupDesc) {
      this.groupDesc = groupDesc;
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

   public static final String ITEM_GROUP = "ITEM_GROUP";

   public static final String GROUP_NAME = "GROUP_NAME";

   public static final String GROUP_DESC = "GROUP_DESC";

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
      return "ItemGroup{" +
         ", bo = " + bo +
         ", site = " + site +
         ", itemGroup = " + itemGroup +
         ", groupName = " + groupName +
         ", groupDesc = " + groupDesc +
         ", createDate = " + createDate +
         ", createUser = " + createUser +
         ", modifyDate = " + modifyDate +
         ", modifyUser = " + modifyUser +
         "}";
   }
}