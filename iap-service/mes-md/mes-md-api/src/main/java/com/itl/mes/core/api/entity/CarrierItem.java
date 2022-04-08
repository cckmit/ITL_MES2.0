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
 * 载具物料表
 * </p>
 *
 * @author space
 * @since 2019-07-22
 */
@TableName("m_carrier_item")
@ApiModel(value="CarrierItem",description="载具物料表")
public class CarrierItem extends Model<CarrierItem> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="CT:SITE,CARRIER_TYPE,SEQ[PK-]")
   @Length( max = 100 )
   @TableId(value = "BO", type = IdType.INPUT)
   private String bo;

   @ApiModelProperty(value="载具类型BO[UK-]")
   @Length( max = 100 )
   @TableField("CARRIER_TYPE_BO")
   private String carrierTypeBo;

   @ApiModelProperty(value="序号")
   @Length( max = 10 )
   @TableField("SEQ")
   private String seq;

   @ApiModelProperty(value="物料BO")
   @Length( max = 100 )
   @TableField("ITEM_BO")
   private String itemBo;

   @ApiModelProperty(value="物料组BO")
   @Length( max = 100 )
   @TableField("ITEM_GROUP_BO")
   private String itemGroupBo;


   public String getBo() {
      return bo;
   }

   public void setBo(String bo) {
      this.bo = bo;
   }

   public String getCarrierTypeBo() {
      return carrierTypeBo;
   }

   public void setCarrierTypeBo(String carrierTypeBo) {
      this.carrierTypeBo = carrierTypeBo;
   }

   public String getSeq() {
      return seq;
   }

   public void setSeq(String seq) {
      this.seq = seq;
   }

   public String getItemBo() {
      return itemBo;
   }

   public void setItemBo(String itemBo) {
      this.itemBo = itemBo;
   }

   public String getItemGroupBo() {
      return itemGroupBo;
   }

   public void setItemGroupBo(String itemGroupBo) {
      this.itemGroupBo = itemGroupBo;
   }

   public static final String BO = "BO";

   public static final String CARRIER_TYPE_BO = "CARRIER_TYPE_BO";

   public static final String SEQ = "SEQ";

   public static final String ITEM_BO = "ITEM_BO";

   public static final String ITEM_GROUP_BO = "ITEM_GROUP_BO";

   @Override
   protected Serializable pkVal() {
      return this.bo;
   }


   @Override
   public String toString() {
      return "CarrierItem{" +
         ", bo = " + bo +
         ", carrierTypeBo = " + carrierTypeBo +
         ", seq = " + seq +
         ", itemBo = " + itemBo +
         ", itemGroupBo = " + itemGroupBo +
         "}";
   }
}