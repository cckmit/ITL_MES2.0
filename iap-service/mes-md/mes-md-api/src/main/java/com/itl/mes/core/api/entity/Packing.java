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
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 包装定义表
 * </p>
 *
 * @author space
 * @since 2019-07-16
 */
@TableName("p_packing")
@ApiModel(value="Packing",description="包装定义表")
public class Packing extends Model<Packing> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="PK:SITE,PACK_NAME【PK】")
   @Length( max = 100 )
   @TableId(value = "BO", type = IdType.INPUT)
   private String bo;

   @ApiModelProperty(value="工厂")
   @Length( max = 32 )
   @TableField("SITE")
   private String site;

   @ApiModelProperty(value="包装名称")
   @Length( max = 32 )
   @TableField("PACK_NAME")
   private String packName;

   @ApiModelProperty(value="包装等级1最大，6最小共六级 必填项")
   @Length( max = 1 )
   @TableField("PACK_GRADE")
   private String packGrade;

   @ApiModelProperty(value="描述")
   @Length( max = 100 )
   @TableField("PACK_DESC")
   private String packDesc;

   @ApiModelProperty(value="最大包装数")
   @TableField("MAX_QTY")
   private BigDecimal maxQty;

   @ApiModelProperty(value="最小包装数")
   @TableField("MIN_QTY")
   private BigDecimal minQty;

   @ApiModelProperty(value="高度")
   @TableField("HEIGHTS")
   private BigDecimal heights;

   @ApiModelProperty(value="长度")
   @TableField("LENGTHS")
   private BigDecimal lengths;

   @ApiModelProperty(value="宽度")
   @TableField("WIDTHS")
   private BigDecimal widths;

   @ApiModelProperty(value="包装重量")
   @TableField("WEIGHTS")
   private BigDecimal weights;

   @ApiModelProperty(value="填充重量")
   @TableField("FILL_WEIGHT")
   private BigDecimal fillWeight;

   @ApiModelProperty(value="状态")
   @Length( max = 1 )
   @TableField("STATE")
   private String state;

   @ApiModelProperty(value="创建人")
   @Length( max = 32 )
   @TableField("CREATE_USER")
   private String createUser;

   @ApiModelProperty(value="创建时间")
   @TableField("CREATE_DATE")
   private Date createDate;

   @ApiModelProperty(value="修改人")
   @Length( max = 32 )
   @TableField("MODIFY_USER")
   private String modifyUser;

   @ApiModelProperty(value="修改时间")
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

   public String getPackName() {
      return packName;
   }

   public void setPackName(String packName) {
      this.packName = packName;
   }

   public String getPackGrade() {
      return packGrade;
   }

   public void setPackGrade(String packGrade) {
      this.packGrade = packGrade;
   }

   public String getPackDesc() {
      return packDesc;
   }

   public void setPackDesc(String packDesc) {
      this.packDesc = packDesc;
   }

   public BigDecimal getMaxQty() {
      return maxQty;
   }

   public void setMaxQty(BigDecimal maxQty) {
      this.maxQty = maxQty;
   }

   public BigDecimal getMinQty() {
      return minQty;
   }

   public void setMinQty(BigDecimal minQty) {
      this.minQty = minQty;
   }

   public BigDecimal getHeights() {
      return heights;
   }

   public void setHeights(BigDecimal heights) {
      this.heights = heights;
   }

   public BigDecimal getLengths() {
      return lengths;
   }

   public void setLengths(BigDecimal lengths) {
      this.lengths = lengths;
   }

   public BigDecimal getWidths() {
      return widths;
   }

   public void setWidths(BigDecimal widths) {
      this.widths = widths;
   }

   public BigDecimal getWeights() {
      return weights;
   }

   public void setWeights(BigDecimal weights) {
      this.weights = weights;
   }

   public BigDecimal getFillWeight() {
      return fillWeight;
   }

   public void setFillWeight(BigDecimal fillWeight) {
      this.fillWeight = fillWeight;
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

   public static final String PACK_NAME = "PACK_NAME";

   public static final String PACK_GRADE = "PACK_GRADE";

   public static final String PACK_DESC = "PACK_DESC";

   public static final String MAX_QTY = "MAX_QTY";

   public static final String MIN_QTY = "MIN_QTY";

   public static final String HEIGHTS = "HEIGHTS";

   public static final String LENGTHS = "LENGTHS";

   public static final String WIDTHS = "WIDTHS";

   public static final String WEIGHTS = "WEIGHTS";

   public static final String FILL_WEIGHT = "FILL_WEIGHT";

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
      return "Packing{" +
         ", bo = " + bo +
         ", site = " + site +
         ", packName = " + packName +
         ", packGrade = " + packGrade +
         ", packDesc = " + packDesc +
         ", maxQty = " + maxQty +
         ", minQty = " + minQty +
         ", heights = " + heights +
         ", lengths = " + lengths +
         ", widths = " + widths +
         ", weights = " + weights +
         ", fillWeight = " + fillWeight +
         ", state = " + state +
         ", createUser = " + createUser +
         ", createDate = " + createDate +
         ", modifyUser = " + modifyUser +
         ", modifyDate = " + modifyDate +
         "}";
   }
}