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

/**
 * <p>
 * 班次时段明细表
 * </p>
 *
 * @author space
 * @since 2019-06-19
 */
@TableName("m_shift_detail")
@ApiModel(value="ShiftDetail",description="班次时段明细表")
public class ShiftDetail extends Model<ShiftDetail> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="SD:SITE,SHIFT,SEQ【PK-】")
   @Length( max = 100 )
   @TableId(value = "BO", type = IdType.INPUT)
   private String bo;

   @ApiModelProperty(value="班次信息，M_SHIFT_INFO表的BO【FK-】")
   @Length( max = 100 )
   @TableField("SHIFT_BO")
   private String shiftBo;

   @ApiModelProperty(value="序号")
   @Length( max = 32 )
   @TableField("SEQ")
   private String seq;

   @ApiModelProperty(value="班次时段开始时间")
   @Length( max = 20 )
   @TableField("SHIFT_START_DATE")
   private String shiftStartDate;

   @ApiModelProperty(value="班次时段结束时间")
   @Length( max = 20 )
   @TableField("SHIFT_END_DATE")
   private String shiftEndDate;

   @ApiModelProperty(value="是否属于当天， Y： 表示当天 N： 表示前一天 ")
   @Length( max = 5 )
   @TableField("IS_CURRENT")
   private String isCurrent;

   @ApiModelProperty(value="说明")
   @Length( max = 50 )
   @TableField("REMARK")
   private String remark;


   public String getBo() {
      return bo;
   }

   public void setBo(String bo) {
      this.bo = bo;
   }

   public String getShiftBo() {
      return shiftBo;
   }

   public void setShiftBo(String shiftBo) {
      this.shiftBo = shiftBo;
   }

   public String getSeq() {
      return seq;
   }

   public void setSeq(String seq) {
      this.seq = seq;
   }

   public String getShiftStartDate() {
      return shiftStartDate;
   }

   public void setShiftStartDate(String shiftStartDate) {
      this.shiftStartDate = shiftStartDate;
   }

   public String getShiftEndDate() {
      return shiftEndDate;
   }

   public void setShiftEndDate(String shiftEndDate) {
      this.shiftEndDate = shiftEndDate;
   }

   public String getIsCurrent() {
      return isCurrent;
   }

   public void setIsCurrent(String isCurrent) {
      this.isCurrent = isCurrent;
   }

   public String getRemark() {
      return remark;
   }

   public void setRemark(String remark) {
      this.remark = remark;
   }

   public static final String BO = "BO";

   public static final String SHIFT_BO = "SHIFT_BO";

   public static final String SEQ = "SEQ";

   public static final String SHIFT_START_DATE = "SHIFT_START_DATE";

   public static final String SHIFT_END_DATE = "SHIFT_END_DATE";

   public static final String IS_CURRENT = "IS_CURRENT";

   public static final String REMARK = "REMARK";

   @Override
   protected Serializable pkVal() {
      return this.bo;
   }


   @Override
   public String toString() {
      return "ShiftDetail{" +
         ", bo = " + bo +
         ", shiftBo = " + shiftBo +
         ", seq = " + seq +
         ", shiftStartDate = " + shiftStartDate +
         ", shiftEndDate = " + shiftEndDate +
         ", isCurrent = " + isCurrent +
         ", remark = " + remark +
         "}";
   }
}