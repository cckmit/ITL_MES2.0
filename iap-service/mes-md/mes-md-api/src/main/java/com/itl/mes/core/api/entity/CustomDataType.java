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
 * 自定义数据类型
 * </p>
 *
 * @author space
 * @since 2019-05-29
 */
@TableName("m_custom_data_type")
@ApiModel(value="CustomDataType",description="自定义数据类型")
public class CustomDataType extends Model<CustomDataType> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="CDT:SITE:CUSTOM_DATA_TYPE")
   @Length( max = 100 )
   @TableId(value = "BO", type = IdType.INPUT)
   private String bo;

   @ApiModelProperty(value="工厂")
   @Length( max = 32 )
   @TableField("SITE")
   private String site;

   @ApiModelProperty(value="自定义数据类型")
   @Length( max = 32 )
   @TableField("CUSTOM_DATA_TYPE")
   private String customDataType;

   @ApiModelProperty(value="自定义数据类型描述")
   @Length( max = 100 )
   @TableField("CUSTOM_DATA_TYPE_DESC")
   private String customDataTypeDesc;

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

   public String getCustomDataTypeDesc() {
      return customDataTypeDesc;
   }

   public void setCustomDataTypeDesc(String customDataTypeDesc) {
      this.customDataTypeDesc = customDataTypeDesc;
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

   public static final String CUSTOM_DATA_TYPE_DESC = "CUSTOM_DATA_TYPE_DESC";

   public static final String CREATE_DATE = "CREATE_DATE";

   public static final String MODIFY_DATE = "MODIFY_DATE";

   @Override
   protected Serializable pkVal() {
      return this.bo;
   }


   public void setObjectDate( String userId,Date date ){
     this.createDate=date;
     this.modifyDate=date;
   }

   @Override
   public String toString() {
      return "CustomDataType{" +
         ", bo = " + bo +
         ", site = " + site +
         ", customDataType = " + customDataType +
         ", customDataTypeDesc = " + customDataTypeDesc +
         ", createDate = " + createDate +
         ", modifyDate = " + modifyDate +
         "}";
   }
}