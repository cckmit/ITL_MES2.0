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

/**
 * <p>
 * 包装级别表
 * </p>
 *
 * @author space
 * @since 2019-07-12
 */
@TableName("p_pack_level")
@ApiModel(value="PackLevel",description="包装级别表")
public class PackLevel extends Model<PackLevel> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="PKL:SITE,PACK_NO,SEQ【PK】")
   @Length( max = 100 )
   @TableId(value = "BO", type = IdType.INPUT)
   private String bo;

   @ApiModelProperty(value="包装BO")
   @Length( max = 100 )
   @TableField("PACKING_BO")
   private String packingBo;

   @ApiModelProperty(value="序号")
   @Length( max = 100 )
   @TableField("SEQ")
   private String seq;

   @ApiModelProperty(value="包装级别")
   @Length( max = 30 )
   @TableField("PACK_LEVEL")
   private String packLevel;

   @ApiModelProperty(value="包装对象BO")
   @Length( max = 100 )
   @TableField("OBJECT_BO")
   private String objectBo;

   @ApiModelProperty(value="工单BO")
   @Length( max = 100 )
   @TableField("SHOP_ORDER_BO")
   private String shopOrderBo;

   @ApiModelProperty(value="最小数量")
   @TableField("MIN_QTY")
   private BigDecimal minQty;

   @ApiModelProperty(value="最大数量")
   @TableField("MAX_QTY")
   private BigDecimal maxQty;


   public String getBo() {
      return bo;
   }

   public void setBo(String bo) {
      this.bo = bo;
   }

   public String getPackingBo() {
      return packingBo;
   }

   public void setPackingBo(String packingBo) {
      this.packingBo = packingBo;
   }

   public String getSeq() {
      return seq;
   }

   public void setSeq(String seq) {
      this.seq = seq;
   }

   public String getPackLevel() {
      return packLevel;
   }

   public void setPackLevel(String packLevel) {
      this.packLevel = packLevel;
   }

   public String getObjectBo() {
      return objectBo;
   }

   public void setObjectBo(String objectBo) {
      this.objectBo = objectBo;
   }

   public String getShopOrderBo() {
      return shopOrderBo;
   }

   public void setShopOrderBo(String shopOrderBo) {
      this.shopOrderBo = shopOrderBo;
   }

   public BigDecimal getMinQty() {
      return minQty;
   }

   public void setMinQty(BigDecimal minQty) {
      this.minQty = minQty;
   }

   public BigDecimal getMaxQty() {
      return maxQty;
   }

   public void setMaxQty(BigDecimal maxQty) {
      this.maxQty = maxQty;
   }

   public static final String BO = "BO";

   public static final String PACKING_BO = "PACKING_BO";

   public static final String SEQ = "SEQ";

   public static final String PACK_LEVEL = "PACK_LEVEL";

   public static final String OBJECT_BO = "OBJECT_BO";

   public static final String SHOP_ORDER_BO = "SHOP_ORDER_BO";

   public static final String MIN_QTY = "MIN_QTY";

   public static final String MAX_QTY = "MAX_QTY";

   @Override
   protected Serializable pkVal() {
      return this.bo;
   }




   @Override
   public String toString() {
      return "PackLevel{" +
         ", bo = " + bo +
         ", packingBo = " + packingBo +
         ", seq = " + seq +
         ", packLevel = " + packLevel +
         ", objectBo = " + objectBo +
         ", shopOrderBo = " + shopOrderBo +
         ", minQty = " + minQty +
         ", maxQty = " + maxQty +
         "}";
   }
}