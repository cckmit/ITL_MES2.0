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
 * 设备类型明细
 * </p>
 *
 * @author space
 * @since 2019-06-17
 */
@TableName("m_device_type_item")
@ApiModel(value="DeviceTypeItem",description="设备类型明细")
public class DeviceTypeItem extends Model<DeviceTypeItem> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="设备类型【对应M_DEVICE_TYPE的BO】【UK】")
   @Length( max = 100 )
   @TableField("DEVICE_TYPE_BO")
   private String deviceTypeBo;

   @ApiModelProperty(value="M_DEVICE 的BO【UK】")
   @Length( max = 100 )
   @TableField("DEVICE_BO")
   private String deviceBo;


   public String getDeviceTypeBo() {
      return deviceTypeBo;
   }

   public void setDeviceTypeBo(String deviceTypeBo) {
      this.deviceTypeBo = deviceTypeBo;
   }

   public String getDeviceBo() {
      return deviceBo;
   }

   public void setDeviceBo(String deviceBo) {
      this.deviceBo = deviceBo;
   }

   public static final String DEVICE_TYPE_BO = "DEVICE_TYPE_BO";

   public static final String DEVICE_BO = "DEVICE_BO";

   @Override
   protected Serializable pkVal() {
      return null;
   }

   @Override
   public String toString() {
      return "DeviceTypeItem{" +
         ", deviceTypeBo = " + deviceTypeBo +
         ", deviceBo = " + deviceBo +
         "}";
   }
}