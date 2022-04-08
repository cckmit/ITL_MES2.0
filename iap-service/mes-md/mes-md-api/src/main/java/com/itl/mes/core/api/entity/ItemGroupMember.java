package com.itl.mes.core.api.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * <p>
 * 物料组成员表
 * </p>
 *
 * @author space
 * @since 2019-05-31
 */
@TableName("m_item_group_member")
@ApiModel(value="ItemGroupMember",description="物料组成员表")
public class ItemGroupMember extends Model<ItemGroupMember> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="物料组【M_ITEM_GROUP的BO】")
   @Length( max = 100 )
   @NotBlank
   @TableField(value = "ITEM_GROUP_BO")
   @Excel( name="物料组【M_ITEM_GROUP的BO】", orderNum="0" )
   private String itemGroupBo;

   @ApiModelProperty(value="物料【M_ITEM的BO】【UK】")
   @Length( max = 100 )
   @NotBlank
   @TableField("ITEM_BO")
   @Excel( name="物料【M_ITEM的BO】【UK】", orderNum="1" )
   private String itemBo;


   public String getItemGroupBo() {
      return itemGroupBo;
   }

   public void setItemGroupBo(String itemGroupBo) {
      this.itemGroupBo = itemGroupBo;
   }

   public String getItemBo() {
      return itemBo;
   }

   public void setItemBo(String itemBo) {
      this.itemBo = itemBo;
   }

   public static final String ITEM_GROUP_BO = "ITEM_GROUP_BO";

   public static final String ITEM_BO = "ITEM_BO";

   @Override
   protected Serializable pkVal() {
      return this.itemGroupBo+this.itemBo;
   }


   @Override
   public String toString() {
      return "ItemGroupMember{" +
         ", itemGroupBo = " + itemGroupBo +
         ", itemBo = " + itemBo +
         "}";
   }
}