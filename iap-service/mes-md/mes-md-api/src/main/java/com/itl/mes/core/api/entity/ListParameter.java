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

/**
 * <p>
 * 
 * </p>
 *
 * @author space
 * @since 2019-06-03
 */
@TableName("m_list_parameter")
@ApiModel(value="ListParameter",description="")
public class ListParameter extends Model<ListParameter> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="LP:DATA_LIST,FIELD_VALUE【PK】")
   @Length( max = 100 )
   @TableId(value = "BO", type = IdType.INPUT)
   private String bo;

   @ApiModelProperty(value="列表类别【UK】")
   @Length( max = 32 )
   @TableField("DATA_LIST_BO")
   private String dataListBo;

   @ApiModelProperty(value="序列")
   @TableField("SEQUENCE")
   private Integer sequence;

   @ApiModelProperty(value="参数值【UK】")
   @Length( max = 64 )
   @TableField("FIELD_VALUE")
   private String fieldValue;

   @ApiModelProperty(value="参数名")
   @Length( max = 50 )
   @TableField("FIELD_NAME")
   private String fieldName;

   @ApiModelProperty(value="默认值")
   @Length( max = 1 )
   @TableField("IS_DEFAULT")
   private String isDefault;


   public String getBo() {
      return bo;
   }

   public void setBo(String bo) {
      this.bo = bo;
   }

   public String getDataListBo() {
      return dataListBo;
   }

   public void setDataListBo(String dataListBo) {
      this.dataListBo = dataListBo;
   }

   public Integer getSequence() {
      return sequence;
   }

   public void setSequence(Integer sequence) {
      this.sequence = sequence;
   }

   public String getFieldValue() {
      return fieldValue;
   }

   public void setFieldValue(String fieldValue) {
      this.fieldValue = fieldValue;
   }

   public String getFieldName() {
      return fieldName;
   }

   public void setFieldName(String fieldName) {
      this.fieldName = fieldName;
   }

   public String getIsDefault() {
      return isDefault;
   }

   public void setIsDefault(String isDefault) {
      this.isDefault = isDefault;
   }

   public static final String BO = "BO";

   public static final String DATA_LIST_BO = "DATA_LIST_BO";

   public static final String SEQUENCE = "SEQUENCE";

   public static final String FIELD_VALUE = "FIELD_VALUE";

   public static final String FIELD_NAME = "FIELD_NAME";

   public static final String IS_DEFAULT = "IS_DEFAULT";

   @Override
   protected Serializable pkVal() {
      return this.bo;
   }


   @Override
   public String toString() {
      return "ListParameter{" +
         ", bo = " + bo +
         ", dataListBo = " + dataListBo +
         ", sequence = " + sequence +
         ", fieldValue = " + fieldValue +
         ", fieldName = " + fieldName +
         ", isDefault = " + isDefault +
         "}";
   }
}