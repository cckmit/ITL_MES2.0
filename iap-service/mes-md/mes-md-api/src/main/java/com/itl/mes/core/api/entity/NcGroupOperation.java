package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * <p>
 * 不合格组工序表
 * </p>
 *
 * @author space
 * @since 2019-06-15
 */
@TableName("m_nc_group_operation")
@ApiModel(value="NcGroupOperation",description="不合格组工序表")
public class NcGroupOperation extends Model<NcGroupOperation> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="不合格组【M_NC_GROUP的BO】【UK】")
   @Length( max = 100 )
   @TableField("NC_GROUP_BO")
   private String ncGroupBo;

   @ApiModelProperty(value="工序【M_OPERATION的BO】【UK】")
   @Length( max = 100 )
   @TableField("OPERATION_BO")
   private String operationBo;


   public String getNcGroupBo() {
      return ncGroupBo;
   }

   public void setNcGroupBo(String ncGroupBo) {
      this.ncGroupBo = ncGroupBo;
   }

   public String getOperationBo() {
      return operationBo;
   }

   public void setOperationBo(String operationBo) {
      this.operationBo = operationBo;
   }

   public static final String NC_GROUP_BO = "NC_GROUP_BO";

   public static final String OPERATION_BO = "OPERATION_BO";



   @Override
   public String toString() {
      return "NcGroupOperation{" +
         ", ncGroupBo = " + ncGroupBo +
         ", operationBo = " + operationBo +
         "}";
   }

   @Override
   protected Serializable pkVal() {
      return null;
   }
}