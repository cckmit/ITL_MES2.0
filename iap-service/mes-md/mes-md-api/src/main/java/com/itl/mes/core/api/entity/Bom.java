package com.itl.mes.core.api.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
 * 物料清单表
 * </p>
 *
 * @author space
 * @since 2019-06-06
 */
@TableName("m_bom")
@ApiModel(value="Bom",description="物料清单表")
public class Bom extends Model<Bom> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="BOM:SITE,BOM,VERSION【PK】")
   @Length( max = 100 )
   @TableId(value = "BO", type = IdType.INPUT)
   private String bo;

   @ApiModelProperty(value="工厂【UK】")
   @Length( max = 32 )
   @TableField("SITE")
   private String site;

   @ApiModelProperty(value="物料清单编号【UK】")
   @Length( max = 64 )
   @TableField("BOM")
   private String bom;

   @ApiModelProperty(value="版本【UK】")
   @Length( max = 64 )
   @TableField("VERSION")
   private String version;

   @ApiModelProperty(value="是否当前版本")
   @Length( max = 1 )
   @TableField("IS_CURRENT_VERSION")
   private String isCurrentVersion;

   @ApiModelProperty(value="物料清单描述")
   @Length( max = 200 )
   @TableField("BOM_DESC")
   private String bomDesc;

   @ApiModelProperty(value="状态【M_STATUS的BO】")
   @Length( max = 100 )
   @TableField("STATE")
   private String state;

   @ApiModelProperty(value="追溯方式")
   @Length( max = 32 )
   @TableField("ZS_TYPE")
   private String zsType;

   @ApiModelProperty(value="创建时间")
   @TableField("CREATE_DATE")
   private Date createDate;

   @ApiModelProperty(value="创建人")
   @Length( max = 32 )
   @TableField("CREATE_USER")
   private String createUser;

   @ApiModelProperty(value="BOM类型 ")
   @TableField("BOM_TYPE")
   private String bomType;
   @ApiModelProperty(value="工单编号")
   @TableField("SHOP_ORDER_BO")
   private String shopOrderBo;
   @ApiModelProperty(value="bom基准用量")
   @TableField("BOM_STANDARD")
   private String bomStandard;
   @ApiModelProperty(value="erp bom")
   @TableField("ERP_BOM")
   private String erpBom;


   @ApiModelProperty(value="修改时间")
   @TableField("MODIFY_DATE")
   private Date modifyDate;

   @ApiModelProperty(value="修改人")
   @Length( max = 32 )
   @TableField("MODIFY_USER")
   private String modifyUser;
   @ApiModelProperty(value = "配方组")
   @Excel(name = "配方组")
   @TableField("FORMULA_GROUP")
   private String formulaGroup;

   @ApiModelProperty(value = "工艺特性")
   @TableField("PROCESS_CHARACTERISTICS")
   private String processCharacteristics;
   @ApiModelProperty(value = "工艺编号")
   @TableField("PROCESS_NUMBER")
   private String processNumber;
   @ApiModelProperty(value = "螺杆组合")
   @TableField("SCREW_ASSEMBLY")
   private String screwAssembly;

   public String getBomType() {
      return bomType;
   }

   public void setBomType(String bomType) {
      this.bomType = bomType;
   }

   public String getShopOrderBo() {
      return shopOrderBo;
   }

   public void setShopOrderBo(String shopOrderBo) {
      this.shopOrderBo = shopOrderBo;
   }

   public String getBomStandard() {
      return bomStandard;
   }

   public void setBomStandard(String bomStandard) {
      this.bomStandard = bomStandard;
   }

   public String getErpBom() {
      return erpBom;
   }

   public void setErpBom(String erpBom) {
      this.erpBom = erpBom;
   }

   public static long getSerialVersionUID() {
      return serialVersionUID;
   }

   public String getFormulaGroup() {
      return formulaGroup;
   }

   public void setFormulaGroup(String formulaGroup) {
      this.formulaGroup = formulaGroup;
   }

   public String getProcessCharacteristics() {
      return processCharacteristics;
   }

   public void setProcessCharacteristics(String processCharacteristics) {
      this.processCharacteristics = processCharacteristics;
   }

   public String getProcessNumber() {
      return processNumber;
   }

   public void setProcessNumber(String processNumber) {
      this.processNumber = processNumber;
   }

   public String getScrewAssembly() {
      return screwAssembly;
   }

   public void setScrewAssembly(String screwAssembly) {
      this.screwAssembly = screwAssembly;
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

   public String getBom() {
      return bom;
   }

   public void setBom(String bom) {
      this.bom = bom;
   }

   public String getVersion() {
      return version;
   }

   public void setVersion(String version) {
      this.version = version;
   }

   public String getIsCurrentVersion() {
      return isCurrentVersion;
   }

   public void setIsCurrentVersion(String isCurrentVersion) {
      this.isCurrentVersion = isCurrentVersion;
   }

   public String getBomDesc() {
      return bomDesc;
   }

   public void setBomDesc(String bomDesc) {
      this.bomDesc = bomDesc;
   }

   public String getState() {
      return state;
   }

   public void setState(String state) {
      this.state = state;
   }

   public String getZsType() {
      return zsType;
   }

   public void setZsType(String zsType) {
      this.zsType = zsType;
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

   public static final String BO = "BO";

   public static final String SITE = "SITE";

   public static final String BOM = "BOM";

   public static final String VERSION = "VERSION";

   public static final String IS_CURRENT_VERSION = "IS_CURRENT_VERSION";

   public static final String BOM_DESC = "BOM_DESC";

   public static final String STATE = "STATE";

   public static final String ZS_TYPE = "ZS_TYPE";

   public static final String CREATE_DATE = "CREATE_DATE";

   public static final String CREATE_USER = "CREATE_USER";

   public static final String MODIFY_DATE = "MODIFY_DATE";

   public static final String MODIFY_USER = "MODIFY_USER";

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
      return "Bom{" +
         ", bo = " + bo +
         ", site = " + site +
         ", bom = " + bom +
         ", version = " + version +
         ", isCurrentVersion = " + isCurrentVersion +
         ", bomDesc = " + bomDesc +
         ", state = " + state +
         ", zsType = " + zsType +
         ", createDate = " + createDate +
         ", createUser = " + createUser +
         ", modifyDate = " + modifyDate +
         ", modifyUser = " + modifyUser +
         "}";
   }
}