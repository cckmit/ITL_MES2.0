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
 * 工序表
 * </p>
 *
 * @author space
 * @since 2019-06-06
 */
@TableName("m_operation")
@ApiModel(value="Operation",description="工序表")
public class Operation extends Model<Operation> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="OP:SITE,OPERATION,VERSION【PK】")
   @Length( max = 200 )
   @TableId(value = "BO", type = IdType.INPUT)
   private String bo;

   @ApiModelProperty(value="工厂【UK】")
   @Length( max = 32 )
   @TableField("SITE")
   private String site;

   @ApiModelProperty(value="工序编号【UK】")
   @Length( max = 64 )
   @TableField("OPERATION")
   private String operation;

   @ApiModelProperty(value="版本号【UK】")
   @Length( max = 3 )
   @TableField("VERSION")
   private String version;

   @ApiModelProperty(value="工序名称")
   @Length( max = 32 )
   @TableField("OPERATION_NAME")
   private String operationName;

   @ApiModelProperty(value="所属产线 BO【UK】")
   @Length( max = 100 )
   @TableField("PRODUCTION_LINE_BO")
   private String productionLineBo;

   @ApiModelProperty(value="描述")
   @Length( max = 200 )
   @TableField("OPERATION_DESC")
   private String operationDesc;

   @ApiModelProperty(value="状态")
   @Length( max = 32 )
   @TableField("STATE")
   private String state;

   @ApiModelProperty(value="是否当前版本 Y是 N否")
   @Length( max = 1 )
   @TableField("IS_CURRENT_VERSION")
   private String isCurrentVersion;

   @ApiModelProperty(value="工序类型 N：普通S：特殊T：测试")
   @Length( max = 1 )
   @TableField("OPERATION_TYPE")
   private String operationType;

   @ApiModelProperty(value="最大经过次数")
   @TableField("MAX_TIMES")
   private Integer maxTimes;

   @ApiModelProperty(value="重测次数")
   @TableField("REPEAT_TEST_TIMES")
   private Integer repeatTestTimes;

   @ApiModelProperty(value="缺省不良代码（BO）")
   @Length( max = 64 )
   @TableField("DEFAULT_NC_CODE_BO")
   private String defaultNcCodeBo;

   @ApiModelProperty(value="不良代码组（BO）")
   @Length( max = 64 )
   @TableField("NC_GROUP_BO")
   private String ncGroupBo;

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

   @ApiModelProperty(value="缺省工位")
   @Length( max = 200 )
   @TableField("DEFAULT_STATION_BO")
   private String defaultStationBo;

   @ApiModelProperty(value="工位类型")
   @Length( max = 200 )
   @TableField("STATION_TYPE_BO")
   private String stationTypeBo;

   @ApiModelProperty(value="部门代号")
   @TableField("DEPT_NO")
   private String deptNo;

   @ApiModelProperty(value="车间")
   @TableField("WORK_SHOP")
   private String workShop;

   @ApiModelProperty("是否被引用（0未被引用，1被引用）")
   @TableField("isUsed")
   private int isUsed;

   public int getIsUsed() {
      return isUsed;
   }

   public void setIsUsed(int isUsed) {
      this.isUsed = isUsed;
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

   public String getOperation() {
      return operation;
   }

   public void setOperation(String operation) {
      this.operation = operation;
   }

   public String getVersion() {
      return version;
   }

   public void setVersion(String version) {
      this.version = version;
   }

   public String getOperationName() {
      return operationName;
   }

   public void setOperationName(String operationName) {
      this.operationName = operationName;
   }

   public String getProductionLineBo() {
      return productionLineBo;
   }

   public void setProductionLineBo(String productionLineBo) {
      this.productionLineBo = productionLineBo;
   }

   public String getOperationDesc() {
      return operationDesc;
   }

   public void setOperationDesc(String operationDesc) {
      this.operationDesc = operationDesc;
   }

   public String getState() {
      return state;
   }

   public void setState(String state) {
      this.state = state;
   }

   public String getIsCurrentVersion() {
      return isCurrentVersion;
   }

   public void setIsCurrentVersion(String isCurrentVersion) {
      this.isCurrentVersion = isCurrentVersion;
   }

   public String getOperationType() {
      return operationType;
   }

   public void setOperationType(String operationType) {
      this.operationType = operationType;
   }

   public Integer getMaxTimes() {
      return maxTimes;
   }

   public void setMaxTimes(Integer maxTimes) {
      this.maxTimes = maxTimes;
   }

   public Integer getRepeatTestTimes() {
      return repeatTestTimes;
   }

   public void setRepeatTestTimes(Integer repeatTestTimes) {
      this.repeatTestTimes = repeatTestTimes;
   }

   public String getDefaultNcCodeBo() {
      return defaultNcCodeBo;
   }

   public void setDefaultNcCodeBo(String defaultNcCodeBo) {
      this.defaultNcCodeBo = defaultNcCodeBo;
   }

   public String getNcGroupBo() {
      return ncGroupBo;
   }

   public void setNcGroupBo(String ncGroupBo) {
      this.ncGroupBo = ncGroupBo;
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

   public String getDefaultStationBo() {
      return defaultStationBo;
   }

   public void setDefaultStationBo(String defaultStationBo) {
      this.defaultStationBo = defaultStationBo;
   }

   public String getStationTypeBo() {
      return stationTypeBo;
   }

   public void setStationTypeBo(String stationTypeBo) {
      this.stationTypeBo = stationTypeBo;
   }

   public String getDeptNo() {
      return deptNo;
   }

   public void setDeptNo(String deptNo) {
      this.deptNo = deptNo;
   }

   public String getWorkShop() {
      return workShop;
   }

   public void setWorkShop(String workShop) {
      this.workShop = workShop;
   }

   public static final String BO = "BO";

   public static final String SITE = "SITE";

   public static final String OPERATION = "OPERATION";

   public static final String VERSION = "VERSION";

   public static final String OPERATION_NAME = "OPERATION_NAME";

   public static final String PRODUCTION_LINE_BO = "PRODUCTION_LINE_BO";

   public static final String OPERATION_DESC = "OPERATION_DESC";

   public static final String STATE = "STATE";

   public static final String IS_CURRENT_VERSION = "IS_CURRENT_VERSION";

   public static final String OPERATION_TYPE = "OPERATION_TYPE";

   public static final String MAX_TIMES = "MAX_TIMES";

   public static final String REPEAT_TEST_TIMES = "REPEAT_TEST_TIMES";

   public static final String DEFAULT_NC_CODE_BO = "DEFAULT_NC_CODE_BO";

   public static final String NC_GROUP_BO = "NC_GROUP_BO";

   public static final String CREATE_DATE = "CREATE_DATE";

   public static final String CREATE_USER = "CREATE_USER";

   public static final String MODIFY_DATE = "MODIFY_DATE";

   public static final String MODIFY_USER = "MODIFY_USER";

   public static final String DEFAULT_STATION_BO = "DEFAULT_STATION_BO";

   public static final String STATION_TYPE_BO = "STATION_TYPE_BO";

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
      return "Operation{" +
         ", bo = " + bo +
         ", site = " + site +
         ", operation = " + operation +
         ", version = " + version +
         ", operationName = " + operationName +
         ", productionLineBo = " + productionLineBo +
         ", operationDesc = " + operationDesc +
         ", state = " + state +
         ", isCurrentVersion = " + isCurrentVersion +
         ", operationType = " + operationType +
         ", maxTimes = " + maxTimes +
         ", repeatTestTimes = " + repeatTestTimes +
         ", defaultNcCodeBo = " + defaultNcCodeBo +
         ", ncGroupBo = " + ncGroupBo +
         ", createDate = " + createDate +
         ", createUser = " + createUser +
         ", modifyDate = " + modifyDate +
         ", modifyUser = " + modifyUser +
         ", defaultStationBo = " + defaultStationBo +
         ", stationTypeBo = " + stationTypeBo +
         "}";
   }
}