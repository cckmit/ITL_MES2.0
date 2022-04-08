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
 * 工位类型明细表
 * </p>
 *
 * @author space
 * @since 2019-06-29
 */
@TableName("m_station_type_item")
@ApiModel(value="StationTypeItem",description="工位类型明细表")
public class StationTypeItem extends Model<StationTypeItem> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="数据类型【对应M_STATION_TYPE的BO】【UK】")
   @Length( max = 100 )
   @TableField("STATION_TYPE_BO")
   private String stationTypeBo;

   @ApiModelProperty(value="M_STATION 的BO【UK】")
   @Length( max = 100 )
   @TableField("STATION_BO")
   private String stationBo;


   public String getStationTypeBo() {
      return stationTypeBo;
   }

   public void setStationTypeBo(String stationTypeBo) {
      this.stationTypeBo = stationTypeBo;
   }

   public String getStationBo() {
      return stationBo;
   }

   public void setStationBo(String stationBo) {
      this.stationBo = stationBo;
   }

   public static final String STATION_TYPE_BO = "STATION_TYPE_BO";

   public static final String STATION_BO = "STATION_BO";




   @Override
   public String toString() {
      return "StationTypeItem{" +
         ", stationTypeBo = " + stationTypeBo +
         ", stationBo = " + stationBo +
         "}";
   }

   @Override
   protected Serializable pkVal() {
      return null;
   }
}