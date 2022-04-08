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
 * 车间日历表
 * </p>
 *
 * @author space
 * @since 2019-06-26
 */
@TableName("m_workshop_date")
@ApiModel(value="WorkshopDate",description="车间日历表")
public class WorkshopDate extends Model<WorkshopDate> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="流水号【PK-】")
   @TableId(value = "BO", type = IdType.AUTO)
   private Integer bo;

   @ApiModelProperty(value="工厂【UK-】")
   @Length( max = 32 )
   @TableField("SITE")
   private String site;

   @ApiModelProperty(value="车间【UK-】")
   @Length( max = 200 )
   @TableField("WORKSHOP_BO")
   private String workshopBo;

   @ApiModelProperty(value="班次")
   @Length( max = 200 )
   @TableField("SHIFT_BO")
   private String shiftBo;

   @ApiModelProperty(value="生产日期")
   @Length( max = 32 )
   @TableField("PRODUCT_DATE")
   private String productDate;

   @ApiModelProperty(value="开始时间")
   @Length( max = 40 )
   @TableField("START_TIME")
   private String startTime;

   @ApiModelProperty(value="结束时间")
   @Length( max = 40 )
   @TableField("END_TIME")
   private String endTime;

   @ApiModelProperty(value="是否加班（1是，0否）")
   @Length( max = 1 )
   @TableField("IS_OVERTIME")
   private String isOvertime;

   @ApiModelProperty(value="创建用户")
   @Length( max = 32 )
   @TableField("CREATE_USER")
   private String createUser;

   @ApiModelProperty(value="创建时间")
   @TableField("CREATE_DATE")
   private Date createDate;

   @ApiModelProperty(value="修改用户")
   @Length( max = 32 )
   @TableField("MODIFY_USER")
   private String modifyUser;

   @ApiModelProperty(value="修改时间")
   @TableField("MODIFY_DATE")
   private Date modifyDate;


   public Integer getBo() {
      return bo;
   }

   public void setBo(Integer bo) {
      this.bo = bo;
   }

   public String getSite() {
      return site;
   }

   public void setSite(String site) {
      this.site = site;
   }

   public String getWorkshopBo() {
      return workshopBo;
   }

   public void setWorkshopBo(String workshopBo) {
      this.workshopBo = workshopBo;
   }

   public String getShiftBo() {
      return shiftBo;
   }

   public void setShiftBo(String shiftBo) {
      this.shiftBo = shiftBo;
   }

   public String getProductDate() {
      return productDate;
   }

   public void setProductDate(String productDate) {
      this.productDate = productDate;
   }

   public String getStartTime() {
      return startTime;
   }

   public void setStartTime(String startTime) {
      this.startTime = startTime;
   }

   public String getEndTime() {
      return endTime;
   }

   public void setEndTime(String endTime) {
      this.endTime = endTime;
   }

   public String getIsOvertime() {
      return isOvertime;
   }

   public void setIsOvertime(String isOvertime) {
      this.isOvertime = isOvertime;
   }

   public String getCreateUser() {
      return createUser;
   }

   public void setCreateUser(String createUser) {
      this.createUser = createUser;
   }

   public Date getCreateDate() {
      return createDate;
   }

   public void setCreateDate(Date createDate) {
      this.createDate = createDate;
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

   public static final String BO = "BO";

   public static final String SITE = "SITE";

   public static final String WORKSHOP_BO = "WORKSHOP_BO";

   public static final String SHIFT_BO = "SHIFT_BO";

   public static final String PRODUCT_DATE = "PRODUCT_DATE";

   public static final String START_TIME = "START_TIME";

   public static final String END_TIME = "END_TIME";

   public static final String IS_OVERTIME = "IS_OVERTIME";

   public static final String CREATE_USER = "CREATE_USER";

   public static final String CREATE_DATE = "CREATE_DATE";

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
      return "WorkshopDate{" +
         ", bo = " + bo +
         ", site = " + site +
         ", workshopBo = " + workshopBo +
         ", shiftBo = " + shiftBo +
         ", productDate = " + productDate +
         ", startTime = " + startTime +
         ", endTime = " + endTime +
         ", isOvertime = " + isOvertime +
         ", createUser = " + createUser +
         ", createDate = " + createDate +
         ", modifyUser = " + modifyUser +
         ", modifyDate = " + modifyDate +
         "}";
   }
}