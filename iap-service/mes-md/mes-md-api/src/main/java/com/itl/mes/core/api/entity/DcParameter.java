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
 * 数据收集组参数表
 * </p>
 *
 * @author space
 * @since 2019-06-04
 */
@TableName("m_dc_parameter")
@ApiModel(value="DcParameter",description="数据收集组参数表")
public class DcParameter extends Model<DcParameter> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="DP:SITE,DC_GROUP,PARAM_NAME【PK】")
   @Length( max = 100 )
   @TableId(value = "BO", type = IdType.INPUT)
   private String bo;

   @ApiModelProperty(value="工厂（冗余字段，将来版本可能删除")
   @Length( max = 32 )
   @TableField("SITE")
   private String site;

   @ApiModelProperty(value="数据收集组【UK】")
   @Length( max = 100 )
   @TableField("DC_GROUP_BO")
   private String dcGroupBo;

   @ApiModelProperty(value="序列")
   @TableField("SEQUENCE")
   private Integer sequence;

   @ApiModelProperty(value="参数名【UK】")
   @Length( max = 50 )
   @TableField("PARAM_NAME")
   private String paramName;

   @ApiModelProperty(value="参数说明")
   @Length( max = 200 )
   @TableField("PARAM_DESC")
   private String paramDesc;

   @ApiModelProperty(value="类型")
   @Length( max = 10 )
   @TableField("PARAM_TYPE")
   private String paramType;

   @ApiModelProperty(value="最大值")
   @TableField("MAX_VALUE")
   private Integer maxValue;

   @ApiModelProperty(value="最小值")
   @TableField("MIN_VALUE")
   private Integer minValue;

   @ApiModelProperty(value="目标值")
   @TableField("TAR_VALUE")
   private Integer tarValue;

   @ApiModelProperty(value="覆盖最大最小值")
   @Length( max = 1 )
   @TableField("CHK_MAX_MIN_VAL")
   private String chkMaxMinVal;

   @ApiModelProperty(value="状态")
   @Length( max = 100 )
   @TableField("STATE")
   private String state;

   @ApiModelProperty(value="是否允许缺少值")
   @Length( max = 1 )
   @TableField("IS_REQUIRED")
   private String isRequired;

   @ApiModelProperty(value="布尔值1")
   @Length( max = 10 )
   @TableField("BOOLEAN_1")
   private String boolean1;

   @ApiModelProperty(value="布尔值2")
   @Length( max = 10 )
   @TableField("BOOLEAN_2")
   private String boolean2;

   @ApiModelProperty(value="参数单位")
   @Length( max = 10 )
   @TableField("UNIT")
   private String unit;


   @ApiModelProperty(value="数据列表（类型为列表时此项必填）")
   @Length( max = 100 )
   @TableField("DATA_LIST_BO")
   private String dataListBo;


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

   public String getDcGroupBo() {
      return dcGroupBo;
   }

   public void setDcGroupBo(String dcGroupBo) {
      this.dcGroupBo = dcGroupBo;
   }

   public Integer getSequence() {
      return sequence;
   }

   public void setSequence(Integer sequence) {
      this.sequence = sequence;
   }

   public String getParamName() {
      return paramName;
   }

   public void setParamName(String paramName) {
      this.paramName = paramName;
   }

   public String getParamDesc() {
      return paramDesc;
   }

   public void setParamDesc(String paramDesc) {
      this.paramDesc = paramDesc;
   }

   public String getParamType() {
      return paramType;
   }

   public void setParamType(String paramType) {
      this.paramType = paramType;
   }

   public Integer getMaxValue() {
      return maxValue;
   }

   public void setMaxValue(Integer maxValue) {
      this.maxValue = maxValue;
   }

   public Integer getMinValue() {
      return minValue;
   }

   public void setMinValue(Integer minValue) {
      this.minValue = minValue;
   }

   public Integer getTarValue() {
      return tarValue;
   }

   public void setTarValue(Integer tarValue) {
      this.tarValue = tarValue;
   }

   public String getChkMaxMinVal() {
      return chkMaxMinVal;
   }

   public void setChkMaxMinVal(String chkMaxMinVal) {
      this.chkMaxMinVal = chkMaxMinVal;
   }

   public String getState() {
      return state;
   }

   public void setState(String state) {
      this.state = state;
   }

   public String getIsRequired() {
      return isRequired;
   }

   public void setIsRequired(String isRequired) {
      this.isRequired = isRequired;
   }

   public String getBoolean1() {
      return boolean1;
   }

   public void setBoolean1(String boolean1) {
      this.boolean1 = boolean1;
   }

   public String getBoolean2() {
      return boolean2;
   }

   public void setBoolean2(String boolean2) {
      this.boolean2 = boolean2;
   }

   public String getUnit() {
      return unit;
   }

   public void setUnit(String unit) {
      this.unit = unit;
   }

   public String getDataListBo() {
      return dataListBo;
   }

   public void setDataListBo(String dataListBo) {
      this.dataListBo = dataListBo;
   }

   public static final String BO = "BO";

   public static final String SITE = "SITE";

   public static final String DC_GROUP_BO = "DC_GROUP_BO";

   public static final String SEQUENCE = "SEQUENCE";

   public static final String PARAM_NAME = "PARAM_NAME";

   public static final String PARAM_DESC = "PARAM_DESC";

   public static final String PARAM_TYPE = "PARAM_TYPE";

   public static final String MAX_VALUE = "MAX_VALUE";

   public static final String MIN_VALUE = "MIN_VALUE";

   public static final String TAR_VALUE = "TAR_VALUE";

   public static final String CHK_MAX_MIN_VAL = "CHK_MAX_MIN_VAL";

   public static final String IS_REQUIRED = "IS_REQUIRED";

   public static final String BOOLEAN_1 = "BOOLEAN_1";

   public static final String BOOLEAN_2 = "BOOLEAN_2";

   public static final String UNIT = "UNIT";

   public static final String DATA_LIST_BO = "DATA_LIST_BO";

   @Override
   protected Serializable pkVal() {
      return this.bo;
   }



   @Override
   public String toString() {
      return "DcParameter{" +
         ", bo = " + bo +
         ", site = " + site +
         ", dcGroupBo = " + dcGroupBo +
         ", sequence = " + sequence +
         ", paramName = " + paramName +
         ", paramDesc = " + paramDesc +
         ", paramType = " + paramType +
         ", maxValue = " + maxValue +
         ", minValue = " + minValue +
         ", tarValue = " + tarValue +
         ", chkMaxMinVal = " + chkMaxMinVal +
         ", state = " + state +
         ", isRequired = " + isRequired +
         ", boolean1 = " + boolean1 +
         ", boolean2 = " + boolean2 +
         ", unit = " + unit +
         ", dataListBo = " + dataListBo +
         "}";
   }
}