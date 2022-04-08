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
 * 不合格代码处理工艺路线表
 * </p>
 *
 * @author space
 * @since 2019-07-08
 */
@TableName("m_nc_disp_router")
@ApiModel(value="NcDispRouter",description="不合格代码处理工艺路线表")
public class NcDispRouter extends Model<NcDispRouter> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="M_NC_CODE BO")
   @Length( max = 100 )
   @TableField("NC_CODE_BO")
   private String ncCodeBo;

   @ApiModelProperty(value="M_ROUTER BO")
   @Length( max = 100 )
   @TableField("ROUTER_BO")
   private String routerBo;


   public String getNcCodeBo() {
      return ncCodeBo;
   }

   public void setNcCodeBo(String ncCodeBo) {
      this.ncCodeBo = ncCodeBo;
   }

   public String getRouterBo() {
      return routerBo;
   }

   public void setRouterBo(String routerBo) {
      this.routerBo = routerBo;
   }

   public static final String NC_CODE_BO = "NC_CODE_BO";

   public static final String ROUTER_BO = "ROUTER_BO";



   @Override
   public String toString() {
      return "NcDispRouter{" +
         ", ncCodeBo = " + ncCodeBo +
         ", routerBo = " + routerBo +
         "}";
   }

   @Override
   protected Serializable pkVal() {
      return null;
   }
}