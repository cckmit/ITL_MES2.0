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
import java.util.Date;

/**
 * <p>
 * 工位表
 * </p>
 *
 * @author space
 * @since 2019-07-12
 */
@TableName("m_station")
@ApiModel(value="Station",description="工位表")
public class Station extends Model<Station> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="WC:SITE,WORK_SHOP,PRODUCT_LINE,OPERATION,STATION【PK】")
   @Length( max = 200 )
   @TableId(value = "BO", type = IdType.INPUT)
   private String bo;

   @ApiModelProperty(value="工厂【UK】")
   @Length( max = 32 )
   @TableField("SITE")
   private String site;

   @ApiModelProperty(value="工位【UK】")
   @Length( max = 64 )
   @TableField("STATION")
   private String station;

   @ApiModelProperty(value="工位名称")
   @Length( max = 32 )
   @TableField("STATION_NAME")
   private String stationName;

   @ApiModelProperty(value="工位描述")
   @Length( max = 200 )
   @TableField("STATION_DESC")
   private String stationDesc;

   @ApiModelProperty(value="所属工序 BO【UK】")
   @Length( max = 100 )
   @TableField("OPERATION_BO")
   private String operationBo;

   @ApiModelProperty(value="状态（已启用1，已禁用0）")
   @Length( max = 1 )
   @TableField("STATE")
   private String state;

   @ApiModelProperty(value="建档日期")
   @TableField("CREATE_DATE")
   private Date createDate;

   @ApiModelProperty(value="建档人")
   @Length( max = 32 )
   @TableField("CREATE_USER")
   private String createUser;

   @ApiModelProperty(value="修改日期")
   @TableField("MODIFY_DATE")
   private Date modifyDate;

   @ApiModelProperty(value="修改人")
   @Length( max = 32 )
   @TableField("MODIFY_USER")
   private String modifyUser;

   @ApiModelProperty(value="工位类型")
   @Length( max = 200 )
   @TableField("STATION_TYPE_BO")
   private String stationTypeBo;

   @ApiModelProperty(value="所属产线")
   @Length( max = 100 )
   @TableField("PRODUCT_LINE_BO")
   private String productLineBo;

   @ApiModelProperty(value="所属工步")
   @TableField("WORKSTATION_BO")
   private String workstationBo;

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

   public String getStation() {
      return station;
   }

   public void setStation(String station) {
      this.station = station;
   }

   public String getStationName() {
      return stationName;
   }

   public void setStationName(String stationName) {
      this.stationName = stationName;
   }

   public String getStationDesc() {
      return stationDesc;
   }

   public void setStationDesc(String stationDesc) {
      this.stationDesc = stationDesc;
   }

   public String getOperationBo() {
      return operationBo;
   }

   public void setOperationBo(String operationBo) {
      this.operationBo = operationBo;
   }

   public String getState() {
      return state;
   }

   public void setState(String state) {
      this.state = state;
   }

   public Date getCreateDate() {
      return createDate;
   }

   public void setCreateDate(Date createDate) {
      this.createDate = createDate;
   }

   public String getWorkstationBo() {
      return workstationBo;
   }

   public void setWorkstationBo(String workstationBo) {
      this.workstationBo = workstationBo;
   }

   public String getCreateUser() {
      return createUser;
   }

   public void setCreateUser(String createUser) {
      this.createUser = createUser;
   }

   public Date getModifyDate() {
      return modifyDate;
   }

   public void setModifyDate(Date modifyDate) {
      this.modifyDate = modifyDate;
   }

   public String getModifyUser() {
      return modifyUser;
   }

   public void setModifyUser(String modifyUser) {
      this.modifyUser = modifyUser;
   }

   public String getStationTypeBo() {
      return stationTypeBo;
   }

   public void setStationTypeBo(String stationTypeBo) {
      this.stationTypeBo = stationTypeBo;
   }

   public String getProductLineBo() {
      return productLineBo;
   }

   public void setProductLineBo(String productLineBo) {
      this.productLineBo = productLineBo;
   }

   public static final String BO = "BO";

   public static final String SITE = "SITE";

   public static final String STATION = "STATION";

   public static final String STATION_NAME = "STATION_NAME";

   public static final String STATION_DESC = "STATION_DESC";

   public static final String OPERATION_BO = "OPERATION_BO";

   public static final String STATE = "STATE";

   public static final String CREATE_DATE = "CREATE_DATE";

   public static final String CREATE_USER = "CREATE_USER";

   public static final String MODIFY_DATE = "MODIFY_DATE";

   public static final String MODIFY_USER = "MODIFY_USER";

   public static final String STATION_TYPE_BO = "STATION_TYPE_BO";

   public static final String PRODUCT_LINE_BO = "PRODUCT_LINE_BO";

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
      return "Station{" +
         ", bo = " + bo +
         ", site = " + site +
         ", station = " + station +
         ", stationName = " + stationName +
         ", stationDesc = " + stationDesc +
         ", operationBo = " + operationBo +
         ", state = " + state +
         ", createDate = " + createDate +
         ", createUser = " + createUser +
         ", modifyDate = " + modifyDate +
         ", modifyUser = " + modifyUser +
         ", stationTypeBo = " + stationTypeBo +
         ", productLineBo = " + productLineBo +
         "}";
   }
}