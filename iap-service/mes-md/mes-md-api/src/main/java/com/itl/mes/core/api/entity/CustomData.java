package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 自定义数据表
 * </p>
 *
 * @author space
 * @since 2019-05-28
 */
@TableName("m_custom_data")
@ApiModel(value="CustomData",description="自定义数据表")
public class CustomData extends Model<CustomData> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="CD:SITE,CUSTOM_DATA_TYPE,CD_FIELD【PK】")
   @Length( max = 100 )
   @TableId(value = "BO", type = IdType.INPUT)
   private String bo;

   @ApiModelProperty(value="工厂【UK】")
   @Length( max = 32 )
   @TableField("SITE")
   private String site;

   @ApiModelProperty(value="自定义数据类别【UK】")
   @Length( max = 32 )
   @TableField("CUSTOM_DATA_TYPE")
   private String customDataType;

   @ApiModelProperty(value="数据字段【UK】")
   @Length( max = 64 )
   @TableField("CD_FIELD")
   private String cdField;

   @ApiModelProperty(value="字段标签")
   @Length( max = 100 )
   @TableField("CD_LABEL")
   private String cdLabel;

   @ApiModelProperty(value="序列",example = "10")
   @NotNull
   @TableField("SEQUENCE")
   private Integer sequence;

   @ApiModelProperty(value="必填,Y:必填，N:非必填")
   @NotBlank
   @Pattern( regexp = "^(Y|N)$" , message = "值必须为Y或者N" )
   @Length( max = 1 )
   @TableField("IS_DATA_REQUIRED")
   private String isDataRequired;

   @ApiModelProperty(value="建档日期")
   @TableField("CREATE_DATE")
   private Date createDate;

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

   public String getCustomDataType() {
      return customDataType;
   }

   public void setCustomDataType(String customDataType) {
      this.customDataType = customDataType;
   }

   public String getCdField() {
      return cdField;
   }

   public void setCdField(String cdField) {
      this.cdField = cdField;
   }

   public String getCdLabel() {
      return cdLabel;
   }

   public void setCdLabel(String cdLabel) {
      this.cdLabel = cdLabel;
   }

   public Integer getSequence() {
      return sequence;
   }

   public void setSequence(Integer sequence) {
      this.sequence = sequence;
   }

   public String getIsDataRequired() {
      return isDataRequired;
   }

   public void setIsDataRequired(String isDataRequired) {
      this.isDataRequired = isDataRequired;
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

   public static final String BO = "BO";

   public static final String SITE = "SITE";

   public static final String CUSTOM_DATA_TYPE = "CUSTOM_DATA_TYPE";

   public static final String CD_FIELD = "CD_FIELD";

   public static final String CD_LABEL = "CD_LABEL";

   public static final String SEQUENCE = "SEQUENCE";

   public static final String IS_DATA_REQUIRED = "IS_DATA_REQUIRED";

   public static final String CREATE_DATE = "CREATE_DATE";

   public static final String MODIFY_DATE = "MODIFY_DATE";

   @Override
   protected Serializable pkVal() {
      return this.bo;
   }


   public void setObjectDate( Date date ){
      this.createDate=date;
      this.modifyDate=date;
   }

   @Override
   public String toString() {
      return "CustomData{" +
              ", bo = " + bo +
              ", site = " + site +
              ", customDataType = " + customDataType +
              ", cdField = " + cdField +
              ", cdLabel = " + cdLabel +
              ", sequence = " + sequence +
              ", isDataRequired = " + isDataRequired +
              ", createDate = " + createDate +
              ", modifyDate = " + modifyDate +
              "}";
   }
}