package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 编码规则表
 * </p>
 *
 * @author space
 * @since 2019-06-19
 */
@TableName("m_code_rule")
@ApiModel(value="CodeRule",description="编码规则表")
public class CodeRule extends Model<CodeRule> {

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

   @ApiModelProperty(value="编码类型")
   @NotBlank
   @Length( max = 32 )
   @TableField("CODE_RULE_TYPE")
   private String codeRuleType;

   @ApiModelProperty(value="描述")
   @Length( max = 50 )
   @TableField("CODE_RULE_DESC")
   private String codeRuleDesc;

   @ApiModelProperty(value="建档人")
   @Length( max = 32 )
   @TableField("CREATE_USER")
   private String createUser;

   @ApiModelProperty(value="建档日期")
   @TableField("CREATE_DATE")
   @JsonFormat(
           pattern = CustomCommonConstants.DATE_FORMAT_CONSTANTS,
           timezone = "GMT+8"
   )
   private Date createDate;

   @ApiModelProperty(value="修改日期")
   @TableField("MODIFY_DATE")
   @JsonFormat(
           pattern = CustomCommonConstants.DATE_FORMAT_CONSTANTS,
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

   public String getCodeRuleType() {
      return codeRuleType;
   }

   public void setCodeRuleType(String codeRuleType) {
      this.codeRuleType = codeRuleType;
   }

   public String getCodeRuleDesc() {
      return codeRuleDesc;
   }

   public void setCodeRuleDesc(String codeRuleDesc) {
      this.codeRuleDesc = codeRuleDesc;
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

   public static final String CODE_RULE_TYPE = "CODE_RULE_TYPE";

   public static final String CODE_RULE_DESC = "CODE_RULE_DESC";

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
      return "CodeRule{" +
         ", bo = " + bo +
         ", site = " + site +
         ", codeRuleType = " + codeRuleType +
         ", codeRuleDesc = " + codeRuleDesc +
         ", createUser = " + createUser +
         ", createDate = " + createDate +
         ", modifyDate = " + modifyDate +
         ", modifyUser = " + modifyUser +
         "}";
   }
}
