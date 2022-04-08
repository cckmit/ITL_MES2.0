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
 * 不合格代码表
 * </p>
 *
 * @author space
 * @since 2019-06-18
 */
@TableName("m_nc_code")
@ApiModel(value="NcCode",description="不合格代码表")
public class NcCode extends Model<NcCode> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="NC:SITE,NC_CODE【PK】")
   @Length( max = 200 )
   @TableId(value = "BO", type = IdType.INPUT)
   private String bo;

   @ApiModelProperty(value="工厂【UK】")
   @Length( max = 32 )
   @TableField("SITE")
   private String site;

   @ApiModelProperty(value="不良代码【UK】")
   @Length( max = 200 )
   @TableField("NC_CODE")
   private String ncCode;

   @ApiModelProperty(value="名称")
   @Length( max = 64 )
   @TableField("NC_NAME")
   private String ncName;

   @ApiModelProperty(value="描述")
   @Length( max = 200 )
   @TableField("NC_DESC")
   private String ncDesc;

   @ApiModelProperty(value="状态")
   @Length( max = 32 )
   @TableField("STATE")
   private String state;

   @ApiModelProperty(value="不良类型[F故障、D缺陷、R修复]")
   @Length( max = 32 )
   @TableField("NC_TYPE")
   private String ncType;

   @ApiModelProperty(value="优先级")
   @TableField("PRIORITY")
   private Integer priority;

   @ApiModelProperty(value="最大不良限制(SFC)")
   @TableField("MAX_NC_LIMIT")
   private Integer maxNcLimit;

   @ApiModelProperty(value="严重性[0无、2低、3中、5高，缺省为3中]")
   @Length( max = 1 )
   @TableField("SEVERITY")
   private String severity;

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

   public String getNcCode() {
      return ncCode;
   }

   public void setNcCode(String ncCode) {
      this.ncCode = ncCode;
   }

   public String getNcName() {
      return ncName;
   }

   public void setNcName(String ncName) {
      this.ncName = ncName;
   }

   public String getNcDesc() {
      return ncDesc;
   }

   public void setNcDesc(String ncDesc) {
      this.ncDesc = ncDesc;
   }

   public String getState() {
      return state;
   }

   public void setState(String state) {
      this.state = state;
   }

   public String getNcType() {
      return ncType;
   }

   public void setNcType(String ncType) {
      this.ncType = ncType;
   }

   public Integer getPriority() {
      return priority;
   }

   public void setPriority(Integer priority) {
      this.priority = priority;
   }

   public Integer getMaxNcLimit() {
      return maxNcLimit;
   }

   public void setMaxNcLimit(Integer maxNcLimit) {
      this.maxNcLimit = maxNcLimit;
   }

   public String getSeverity() {
      return severity;
   }

   public void setSeverity(String severity) {
      this.severity = severity;
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

   public static final String NC_CODE = "NC_CODE";

   public static final String NC_NAME = "NC_NAME";

   public static final String NC_DESC = "NC_DESC";

   public static final String STATE = "STATE";

   public static final String NC_TYPE = "NC_TYPE";

   public static final String PRIORITY = "PRIORITY";

   public static final String MAX_NC_LIMIT = "MAX_NC_LIMIT";

   public static final String SEVERITY = "SEVERITY";

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
      return "NcCode{" +
         ", bo = " + bo +
         ", site = " + site +
         ", ncCode = " + ncCode +
         ", ncName = " + ncName +
         ", ncDesc = " + ncDesc +
         ", state = " + state +
         ", ncType = " + ncType +
         ", priority = " + priority +
         ", maxNcLimit = " + maxNcLimit +
         ", severity = " + severity +
         ", createDate = " + createDate +
         ", createUser = " + createUser +
         ", modifyDate = " + modifyDate +
         ", modifyUser = " + modifyUser +
         "}";
   }
}