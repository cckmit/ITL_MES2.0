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
 * 班次信息主表
 * </p>
 *
 * @author space
 * @since 2019-06-19
 */
@TableName("m_shift")
@ApiModel(value="Shift",description="班次信息主表")
public class Shift extends Model<Shift> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="SHIFT:SITE,SHIFT【PK-】")
   @Length( max = 100 )
   @TableId(value = "BO", type = IdType.INPUT)
   private String bo;

   @ApiModelProperty(value="工厂【UK-】")
   @Length( max = 32 )
   @TableField("SITE")
   private String site;

   @ApiModelProperty(value="班次【UK-】")
   @Length( max = 32 )
   @TableField("SHIFT")
   private String shift;

   @ApiModelProperty(value="班次名称")
   @Length( max = 100 )
   @TableField("SHIFT_NAME")
   private String shiftName;

   @ApiModelProperty(value="班次描述")
   @Length( max = 412 )
   @TableField("SHIFT_DESC")
   private String shiftDesc;

   @ApiModelProperty(value="是否有效 Y：表示当前班次有效 N：表示当前班次无效 ")
   @Length( max = 5 )
   @TableField("IS_VALID")
   private String isValid;

   @ApiModelProperty(value="班次工作时间，单位为小时")
   @TableField("WORK_TIME")
   private Integer workTime;

   @ApiModelProperty(value="创建人")
   @Length( max = 32 )
   @TableField("CREATE_USER")
   private String createUser;

   @ApiModelProperty(value="创建时间")
   @TableField("CREATE_DATE")
   private Date createDate;

   @ApiModelProperty(value="修改人")
   @Length( max = 32 )
   @TableField("MODIFY_USER")
   private String modifyUser;

   @ApiModelProperty(value="修改时间")
   @TableField("MODIFY_DATE")
   private Date modifyDate;


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

   public String getShift() {
      return shift;
   }

   public void setShift(String shift) {
      this.shift = shift;
   }

   public String getShiftName() {
      return shiftName;
   }

   public void setShiftName(String shiftName) {
      this.shiftName = shiftName;
   }

   public String getShiftDesc() {
      return shiftDesc;
   }

   public void setShiftDesc(String shiftDesc) {
      this.shiftDesc = shiftDesc;
   }

   public String getIsValid() {
      return isValid;
   }

   public void setIsValid(String isValid) {
      this.isValid = isValid;
   }

   public Integer getWorkTime() {
      return workTime;
   }

   public void setWorkTime(Integer workTime) {
      this.workTime = workTime;
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

   public static final String SHIFT = "SHIFT";

   public static final String SHIFT_NAME = "SHIFT_NAME";

   public static final String SHIFT_DESC = "SHIFT_DESC";

   public static final String IS_VALID = "IS_VALID";

   public static final String WORK_TIME = "WORK_TIME";

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
      return "Shift{" +
         ", bo = " + bo +
         ", site = " + site +
         ", shift = " + shift +
         ", shiftName = " + shiftName +
         ", shiftDesc = " + shiftDesc +
         ", isValid = " + isValid +
         ", workTime = " + workTime +
         ", createUser = " + createUser +
         ", createDate = " + createDate +
         ", modifyUser = " + modifyUser +
         ", modifyDate = " + modifyDate +
         "}";
   }
}