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
 * 不合格组成员表
 * </p>
 *
 * @author space
 * @since 2019-06-15
 */
@TableName("m_nc_group_member")
@ApiModel(value="NcGroupMember",description="不合格组成员表")
public class NcGroupMember extends Model<NcGroupMember> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="不合格组【M_NC_GROUP的BO】【UK】")
   @Length( max = 100 )
   @TableField("NC_GROUP_BO")
   private String ncGroupBo;

   @ApiModelProperty(value="不合格代码【M_NC_CODE的BO】【UK】")
   @Length( max = 100 )
   @TableField("NC_CODE_BO")
   private String ncCodeBo;


   public String getNcGroupBo() {
      return ncGroupBo;
   }

   public void setNcGroupBo(String ncGroupBo) {
      this.ncGroupBo = ncGroupBo;
   }

   public String getNcCodeBo() {
      return ncCodeBo;
   }

   public void setNcCodeBo(String ncCodeBo) {
      this.ncCodeBo = ncCodeBo;
   }

   public static final String NC_GROUP_BO = "NC_GROUP_BO";

   public static final String NC_CODE_BO = "NC_CODE_BO";



   @Override
   public String toString() {
      return "NcGroupMember{" +
         ", ncGroupBo = " + ncGroupBo +
         ", ncCodeBo = " + ncCodeBo +
         "}";
   }

   @Override
   protected Serializable pkVal() {
      return null;
   }
}