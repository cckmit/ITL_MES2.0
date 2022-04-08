package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 设备表
 * </p>
 *
 * @author space
 * @since 2019-06-17
 */
@Data
@TableName("m_device")
@ApiModel(value="Device",description="设备表")
public class Device extends Model<Device> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="RES:SITE,DEVICE【PK】")
   @Length( max = 100 )
   @TableId(value = "BO", type = IdType.INPUT)
   private String bo;

   @ApiModelProperty(value="工厂【UK】")
   @Length( max = 32 )
   @TableField("SITE")
   private String site;

   @ApiModelProperty(value="设备编号【UK】")
   @Length( max = 64 )
   @TableField("DEVICE")
   private String device;

   @ApiModelProperty(value="设备名称")
   @Length( max = 100 )
   @TableField("DEVICE_NAME")
   private String deviceName;

   @ApiModelProperty(value="设备描述")
   @Length( max = 200 )
   @TableField("DEVICE_DESC")
   private String deviceDesc;

   @ApiModelProperty(value="设备型号")
   @Length( max = 30 )
   @TableField("DEVICE_MODEL")
   private String deviceModel;

   @ApiModelProperty(value="状态")
   @Length( max = 100 )
   @TableField("STATE")
   private String state;

//   @ApiModelProperty(value="是否为加工设备")
//   @Length( max = 1 )
//   @TableField("IS_PROCESS_DEVICE")
//   private String isProcessDevice;

   @ApiModelProperty(value="产线【M_PRODUCT_LINE的BO】")
   @Length( max = 100 )
   @TableField("PRODUCT_LINE_BO")
   private String productLineBo;

   @ApiModelProperty(value="工位【M_STATION的BO】")
   @Length( max = 100 )
   @TableField("STATION_BO")
   private String stationBo;

   @ApiModelProperty(value="安装地点")
   @Length( max = 100 )
   @TableField("LOCATION")
   private String location;

   @ApiModelProperty(value="资产编号")
   @Length( max = 64 )
   @TableField("ASSETS_CODE")
   private String assetsCode;

   @ApiModelProperty(value="生产厂家")
   @Length( max = 100 )
   @TableField("MANUFACTURER")
   private String manufacturer;

   @ApiModelProperty(value="投产日期")
   @TableField("VALID_START_DATE")
   @JsonFormat(
           pattern = "yyyy-MM-dd",
           timezone = "GMT+8"
   )
   private Date validStartDate;

//   @ApiModelProperty(value="结束日期")
//   @TableField("VALID_END_DATE")
//   private Date validEndDate;

   @ApiModelProperty(value="备注")
   @Length( max = 200 )
   @TableField("MEMO")
   private String memo;

   @ApiModelProperty(value="建档日期")
   @TableField("CREATE_DATE")
   @JsonFormat(
           pattern = "yyyy-MM-dd",
           timezone = "GMT+8"
   )
   private Date createDate;

   @ApiModelProperty(value="建档人")
   @Length( max = 32 )
   @TableField("CREATE_USER")
   private String createUser;

   @ApiModelProperty(value="修改人")
   @Length( max = 32 )
   @TableField("MODIFY_USER")
   private String modifyUser;

   @ApiModelProperty(value="修改日期")
   @TableField("MODIFY_DATE")
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
   private Date modifyDate;

   @ApiModelProperty(value="车间")
   @TableField("WORK_SHOP")
   private String workShop;

   @ApiModelProperty(value="车间名称")
   @TableField("WORK_SHOP_NAME")
   private String workShopName;

//   @ApiModelProperty(value="螺杆组合")
//   @TableField("SCREW_COMBINATION")
//   private String screwCombination;
   @ApiModelProperty(value="生产日期")
   @TableField("PRODUCTION_DATE")
   @JsonFormat(
           pattern = "yyyy-MM-dd",
           timezone = "GMT+8"
   )
   private Date productionDate;

   @ApiModelProperty(value="进厂日期")
   @JsonFormat(
           pattern = "yyyy-MM-dd",
           timezone = "GMT+8"
   )
   @TableField("JOIN_FACTORY_DATE")
   private Date joinFactoryDate;

   @ApiModelProperty(value="工序【M_OPERATION的BO】")
   @TableField("OPERATION_BO")
   private String operationBo;

   @ApiModelProperty(value="设备类型")
   @TableField("DEVICE_TYPE")
   private String deviceType;

   @ApiModelProperty(value="设备负责人")
   @TableField("RESPONSIBLE_PERSON")
   private String responsiblePerson;

   @ApiModelProperty(value="设备图片")
   @TableField("DEVICE_IMAGE")
   private String deviceImage;

   @ApiModelProperty(value="产品（物料）BO")
   @TableField("ITEM_BO")
   private String itemBo;

   @ApiModelProperty(value="产品（物料）名称")
   @TableField(exist = false)
   private String itemName;

   @ApiModelProperty(value="产品（物料）编码")
   @TableField(exist = false)
   private String item;

   @TableField(exist = false)
   private String responsiblePersonName;

   @ApiModelProperty("工序名称")
   @TableField(exist = false)
   private String operationName;

   @ApiModelProperty(value="字段派工系数")
   @TableField("DISPATCH_RATIO")
   private BigDecimal dispatchRatio;

   @ApiModelProperty(value="首工序写入时间（生产执行）")
   @TableField("SFC_IN_TIME")
   @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
   private Date sfcInTime;

   @ApiModelProperty("车间BO")
   @TableField(exist = false)
   private String workShopBo;

   @ApiModelProperty(value="设备类型名称")
   @TableField(exist = false)
   private String deviceTypeName;

   @ApiModelProperty(value="设备类型BO")
   @TableField(exist = false)
   private String deviceTypeBo;

   @ApiModelProperty("设备类型")
   @TableField(exist = false)
   List<DeviceType> deviceTypeList;

   @ApiModelProperty("产线名称")
   @TableField(exist = false)
   private String productLineDesc;

   public BigDecimal getDispatchRatio() {
      return dispatchRatio;
   }

   public void setDispatchRatio(BigDecimal dispatchRatio) {
      this.dispatchRatio = dispatchRatio;
   }

   public String getResponsiblePersonName() {
      return responsiblePersonName;
   }

   public void setResponsiblePersonName(String responsiblePersonName) {
      this.responsiblePersonName = responsiblePersonName;
   }

   public String getWorkShop() {
      return workShop;
   }

   public void setWorkShop(String workShop) {
      this.workShop = workShop;
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

   public String getDevice() {
      return device;
   }

   public void setDevice(String device) {
      this.device = device;
   }

   public String getDeviceName() {
      return deviceName;
   }

   public void setDeviceName(String deviceName) {
      this.deviceName = deviceName;
   }

   public String getDeviceDesc() {
      return deviceDesc;
   }

   public void setDeviceDesc(String deviceDesc) {
      this.deviceDesc = deviceDesc;
   }

   public String getDeviceModel() {
      return deviceModel;
   }

   public void setDeviceModel(String deviceModel) {
      this.deviceModel = deviceModel;
   }

   public String getState() {
      return state;
   }

   public void setState(String state) {
      this.state = state;
   }

   public String getProductLineBo() {
      return productLineBo;
   }

   public void setProductLineBo(String productLineBo) {
      this.productLineBo = productLineBo;
   }

   public String getStationBo() {
      return stationBo;
   }

   public void setStationBo(String stationBo) {
      this.stationBo = stationBo;
   }

   public String getLocation() {
      return location;
   }

   public void setLocation(String location) {
      this.location = location;
   }

   public String getAssetsCode() {
      return assetsCode;
   }

   public void setAssetsCode(String assetsCode) {
      this.assetsCode = assetsCode;
   }

   public String getManufacturer() {
      return manufacturer;
   }

   public void setManufacturer(String manufacturer) {
      this.manufacturer = manufacturer;
   }

   public Date getValidStartDate() {
      return validStartDate;
   }

   public void setValidStartDate(Date validStartDate) {
      this.validStartDate = validStartDate;
   }

   public String getMemo() {
      return memo;
   }

   public void setMemo(String memo) {
      this.memo = memo;
   }

   public Date getCreateDate() {
      return createDate;
   }

   public void setCreateDate(Date createDate) {
      this.createDate = createDate;
   }

   public String getCreateUser() {
      return createUser;
   }

   public void setCreateUser(String createUser) {
      this.createUser = createUser;
   }

   public String getModifyUser() {
      return modifyUser;
   }

   public void setModifyUser(String modifyUser) {
      this.modifyUser = modifyUser;
   }

   public Date getModifyDate() {
      return modifyDate;
   }

   public void setModifyDate(Date modifyDate) {
      this.modifyDate = modifyDate;
   }

   public String getResponsiblePerson() {
      return responsiblePerson;
   }

   public void setResponsiblePerson(String responsiblePerson) {
      this.responsiblePerson = responsiblePerson;
   }

   public String getDeviceImage() {
      return deviceImage;
   }

   public Date getProductionDate() {
      return productionDate;
   }

   public void setProductionDate(Date productionDate) {
      this.productionDate = productionDate;
   }

   public Date getJoinFactoryDate() {
      return joinFactoryDate;
   }

   public void setJoinFactoryDate(Date joinFactoryDate) {
      this.joinFactoryDate = joinFactoryDate;
   }

   public String getOperationBo() {
      return operationBo;
   }

   public void setOperationBo(String operationBo) {
      this.operationBo = operationBo;
   }

   public String getDeviceType() {
      return deviceType;
   }

   public void setDeviceType(String deviceType) {
      this.deviceType = deviceType;
   }

   public void setDeviceImage(String deviceImage) {
      this.deviceImage = deviceImage;
   }

   public Date getSfcInTime() {
      return sfcInTime;
   }

   public void setSfcInTime(Date sfcInTime) {
      this.sfcInTime = sfcInTime;
   }

   public static final String BO = "BO";

   public static final String SITE = "SITE";

   public static final String DEVICE = "DEVICE";

   public static final String DEVICE_NAME = "DEVICE_NAME";

   public static final String DEVICE_DESC = "DEVICE_DESC";

   public static final String DEVICE_MODEL = "DEVICE_MODEL";

   public static final String STATE = "STATE";

   public static final String IS_PROCESS_DEVICE = "IS_PROCESS_DEVICE";

   public static final String PRODUCT_LINE_BO = "PRODUCT_LINE_BO";

   public static final String STATION_BO = "STATION_BO";

   public static final String LOCATION = "LOCATION";

   public static final String ASSETS_CODE = "ASSETS_CODE";

   public static final String MANUFACTURER = "MANUFACTURER";

   public static final String VALID_START_DATE = "VALID_START_DATE";

   public static final String VALID_END_DATE = "VALID_END_DATE";

   public static final String MEMO = "MEMO";

   public static final String CREATE_DATE = "CREATE_DATE";

   public static final String CREATE_USER = "CREATE_USER";

   public static final String MODIFY_USER = "MODIFY_USER";

   public static final String MODIFY_DATE = "MODIFY_DATE";

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
      return "Device{" +
              "bo='" + bo + '\'' +
              ", site='" + site + '\'' +
              ", device='" + device + '\'' +
              ", deviceName='" + deviceName + '\'' +
              ", deviceDesc='" + deviceDesc + '\'' +
              ", deviceModel='" + deviceModel + '\'' +
              ", state='" + state + '\'' +
              ", productLineBo='" + productLineBo + '\'' +
              ", stationBo='" + stationBo + '\'' +
              ", location='" + location + '\'' +
              ", assetsCode='" + assetsCode + '\'' +
              ", manufacturer='" + manufacturer + '\'' +
              ", validStartDate=" + validStartDate +
              ", memo='" + memo + '\'' +
              ", createDate=" + createDate +
              ", createUser='" + createUser + '\'' +
              ", modifyUser='" + modifyUser + '\'' +
              ", modifyDate=" + modifyDate +
              ", workShop='" + workShop + '\'' +
              ", productionDate=" + productionDate +
              ", joinFactoryDate=" + joinFactoryDate +
              ", operationBo='" + operationBo + '\'' +
              ", deviceType='" + deviceType + '\'' +
              ", responsiblePerson='" + responsiblePerson + '\'' +
              ", deviceImage='" + deviceImage + '\'' +
              '}';
   }
}