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
 * 物料清单组件
 * </p>
 *
 * @author space
 * @since 2019-07-10
 */
@TableName("m_bom_componnet")
@ApiModel(value="BomComponnet",description="物料清单组件")
public class BomComponnet extends Model<BomComponnet> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="BC:SITE,BOM,COMPONENT【PK】")
   @Length( max = 100 )
   @TableId(value = "BO", type = IdType.INPUT)
   private String bo;

   @ApiModelProperty(value="工厂")
   @Length( max = 32 )
   @TableField("SITE")
   private String site;

   @ApiModelProperty(value="BOM【M_BOM的BO】【UK】【FK-】")
   @Length( max = 100 )
   @TableField("BOM_BO")
   private String bomBo;

   @ApiModelProperty(value="编号【UK】")
   @TableField("SEQUENCE")
   private Integer sequence;

   @ApiModelProperty(value="组件【M_ITEM的BO】")
   @Length( max = 100 )
   @TableField("COMPONENT_BO")
   private String componentBo;

   @ApiModelProperty(value="物料单位")
   @Length( max = 100 )
   @TableField("ITEM_UNIT")
   private String itemUnit;

   @ApiModelProperty(value="装配工序【M_OPERATION的BO】")
   @Length( max = 100 )
   @TableField("OPERATION_BO")
   private String operationBo;

   @ApiModelProperty(value="是否使用替代组件")
   @Length( max = 1 )
   @TableField("IS_USE_ALTERNATE_COMPONENT")
   private String isUseAlternateComponent;

   @ApiModelProperty(value="追溯方式")
   @Length( max = 1 )
   @TableField("ZS_TYPE")
   private String zsType;

   @ApiModelProperty(value="装配类型")
   @Length( max = 32 )
   @TableField("ASS_TYPE")
   private String assType;

   @ApiModelProperty(value="组件位置")
   @Length( max = 64 )
   @TableField("COMPONENT_POSITION")
   private String componentPosition;

   @ApiModelProperty(value="参考指示符")
   @Length( max = 30 )
   @TableField("REFERENCE")
   private String reference;

   @ApiModelProperty(value="是否有子BOM")
   @Length( max = 1 )
   @TableField("HAS_SUB_BOM")
   private String hasSubBom;

   @ApiModelProperty(value="是否有子BOM")
   @Length( max = 100 )
   @TableField("SUB_BOM_BO")
   private String subBomBo;

   @ApiModelProperty(value="装配数量")
   @TableField("QTY")
   private BigDecimal qty;
   @ApiModelProperty(value="组件类型")
   @TableField("ITEM_TYPE")
   private String itemType;
   @ApiModelProperty(value="组件装配顺序")
   @TableField("ITEM_ORDER")
   private String itemOrder;
   @ApiModelProperty(value="虚拟件支持")
   @TableField("VIRTUAL_ITEM")
   private String virtualItem;

   @ApiModelProperty(value="物料描述")
   @TableField("ITEM_DESC")
   private String itemDesc;

   public static long getSerialVersionUID() {
      return serialVersionUID;
   }

   public String getItemType() {
      return itemType;
   }

   public void setItemType(String itemType) {
      this.itemType = itemType;
   }

   public String getItemOrder() {
      return itemOrder;
   }

   public void setItemOrder(String itemOrder) {
      this.itemOrder = itemOrder;
   }

   public String getVirtualItem() {
      return virtualItem;
   }

   public void setVirtualItem(String virtualItem) {
      this.virtualItem = virtualItem;
   }

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

   public String getBomBo() {
      return bomBo;
   }

   public void setBomBo(String bomBo) {
      this.bomBo = bomBo;
   }

   public Integer getSequence() {
      return sequence;
   }

   public void setSequence(Integer sequence) {
      this.sequence = sequence;
   }

   public String getComponentBo() {
      return componentBo;
   }

   public void setComponentBo(String componentBo) {
      this.componentBo = componentBo;
   }

   public String getItemUnit() {
      return itemUnit;
   }

   public void setItemUnit(String itemUnit) {
      this.itemUnit = itemUnit;
   }

   public String getOperationBo() {
      return operationBo;
   }

   public void setOperationBo(String operationBo) {
      this.operationBo = operationBo;
   }

   public String getIsUseAlternateComponent() {
      return isUseAlternateComponent;
   }

   public void setIsUseAlternateComponent(String isUseAlternateComponent) {
      this.isUseAlternateComponent = isUseAlternateComponent;
   }

   public String getZsType() {
      return zsType;
   }

   public void setZsType(String zsType) {
      this.zsType = zsType;
   }

   public String getAssType() {
      return assType;
   }

   public void setAssType(String assType) {
      this.assType = assType;
   }

   public String getComponentPosition() {
      return componentPosition;
   }

   public void setComponentPosition(String componentPosition) {
      this.componentPosition = componentPosition;
   }

   public String getReference() {
      return reference;
   }

   public void setReference(String reference) {
      this.reference = reference;
   }

   public String getHasSubBom() {
      return hasSubBom;
   }

   public void setHasSubBom(String hasSubBom) {
      this.hasSubBom = hasSubBom;
   }

   public String getSubBomBo() {
      return subBomBo;
   }

   public void setSubBomBo(String subBomBo) {
      this.subBomBo = subBomBo;
   }

   public BigDecimal getQty() {
      return qty;
   }

   public void setQty(BigDecimal qty) {
      this.qty = qty;
   }

   public String getItemDesc() {
      return itemDesc;
   }

   public void setItemDesc(String itemDesc) {
      this.itemDesc = itemDesc;
   }

   public static final String BO = "BO";

   public static final String SITE = "SITE";

   public static final String BOM_BO = "BOM_BO";

   public static final String SEQUENCE = "SEQUENCE";

   public static final String COMPONENT_BO = "COMPONENT_BO";

   public static final String ITEM_UNIT = "ITEM_UNIT";

   public static final String OPERATION_BO = "OPERATION_BO";

   public static final String IS_USE_ALTERNATE_COMPONENT = "IS_USE_ALTERNATE_COMPONENT";

   public static final String ZS_TYPE = "ZS_TYPE";

   public static final String ASS_TYPE = "ASS_TYPE";

   public static final String COMPONENT_POSITION = "COMPONENT_POSITION";

   public static final String REFERENCE = "REFERENCE";

   public static final String HAS_SUB_BOM = "HAS_SUB_BOM";

   public static final String SUB_BOM_BO = "SUB_BOM_BO";

   public static final String QTY = "QTY";

   @Override
   protected Serializable pkVal() {
      return this.bo;
   }



   @Override
   public String toString() {
      return "BomComponnet{" +
         ", bo = " + bo +
         ", site = " + site +
         ", bomBo = " + bomBo +
         ", sequence = " + sequence +
         ", componentBo = " + componentBo +
         ", itemUnit = " + itemUnit +
         ", operationBo = " + operationBo +
         ", isUseAlternateComponent = " + isUseAlternateComponent +
         ", zsType = " + zsType +
         ", assType = " + assType +
         ", componentPosition = " + componentPosition +
         ", reference = " + reference +
         ", hasSubBom = " + hasSubBom +
         ", subBomBo = " + subBomBo +
         ", qty = " + qty +
         "}";
   }
}