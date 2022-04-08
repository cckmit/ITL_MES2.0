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
 * 不合格代码组表
 * </p>
 *
 * @author space
 * @since 2019-05-24
 */
@TableName("m_nc_group")
@ApiModel(value="NcGroup",description="不合格代码组表")
public class NcGroup extends Model<NcGroup> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="NG:SITE,NC_GROUP【PK】")
   @Length( max = 200 )
   @TableId(value = "BO", type = IdType.INPUT)
   private String bo;

   @ApiModelProperty(value="工厂【UK】")
   @Length( max = 32 )
   @TableField("SITE")
   private String site;

   @ApiModelProperty(value="不合格代码组【UK】")
   @Length( max = 200 )
   @TableField("NC_GROUP")
   private String ncGroup;

   @ApiModelProperty(value="名称")
   @Length( max = 64 )
   @TableField("NC_GROUP_NAME")
   private String ncGroupName;

   @ApiModelProperty(value="描述")
   @Length( max = 200 )
   @TableField("NC_GROUP_DESC")
   private String ncGroupDesc;

   @ApiModelProperty(value="是否在所有资源上有效")
   @Length( max = 1 )
   @TableField("IS_ALL_RESOURCE")
   private String isAllResource;

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

   public String getNcGroup() {
      return ncGroup;
   }

   public void setNcGroup(String ncGroup) {
      this.ncGroup = ncGroup;
   }

   public String getNcGroupName() {
      return ncGroupName;
   }

   public void setNcGroupName(String ncGroupName) {
      this.ncGroupName = ncGroupName;
   }

   public String getNcGroupDesc() {
      return ncGroupDesc;
   }

   public void setNcGroupDesc(String ncGroupDesc) {
      this.ncGroupDesc = ncGroupDesc;
   }

   public String getIsAllResource() {
      return isAllResource;
   }

   public void setIsAllResource(String isAllResource) {
      this.isAllResource = isAllResource;
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

   public static final String NC_GROUP = "NC_GROUP";

   public static final String NC_GROUP_NAME = "NC_GROUP_NAME";

   public static final String NC_GROUP_DESC = "NC_GROUP_DESC";

   public static final String IS_ALL_RESOURCE = "IS_ALL_RESOURCE";

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
      return "NcGroup{" +
         ", bo = " + bo +
         ", site = " + site +
         ", ncGroup = " + ncGroup +
         ", ncGroupName = " + ncGroupName +
         ", ncGroupDesc = " + ncGroupDesc +
         ", isAllResource = " + isAllResource +
         ", createDate = " + createDate +
         ", createUser = " + createUser +
         ", modifyDate = " + modifyDate +
         ", modifyUser = " + modifyUser +
         "}";
   }
}