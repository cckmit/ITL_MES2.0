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
 * 数据列表表
 * </p>
 *
 * @author space
 * @since 2019-06-03
 */
@TableName("m_data_list")
@ApiModel(value="DataList",description="数据列表表")
public class DataList extends Model<DataList> {

   private static final long serialVersionUID = 1L;


   @ApiModelProperty(value="DL:DATA_LIST【PK】")
   @Length( max = 64 )
   @TableId(value = "BO", type = IdType.INPUT)
   private String bo;

   @ApiModelProperty(value="列表编号【PK】")
   @Length( max = 64 )
   @TableField("DATA_LIST")
   private String dataList;

   @ApiModelProperty(value="列表名")
   @Length( max = 50 )
   @TableField("LIST_NAME")
   private String listName;

   @ApiModelProperty(value="列表描述")
   @Length( max = 200 )
   @TableField("LIST_DESC")
   private String listDesc;

   @ApiModelProperty(value="创建时间")
   @TableField("CREATE_DATE")
   private Date createDate;

   @ApiModelProperty(value="修改日期")
   @TableField("MODIFY_DATE")
   private Date modifyDate;


   public String getBo() {
      return bo;
   }

   public void setBo(String bo) {
      this.bo = bo;
   }

   public String getDataList() {
      return dataList;
   }

   public void setDataList(String dataList) {
      this.dataList = dataList;
   }

   public String getListName() {
      return listName;
   }

   public void setListName(String listName) {
      this.listName = listName;
   }

   public String getListDesc() {
      return listDesc;
   }

   public void setListDesc(String listDesc) {
      this.listDesc = listDesc;
   }

   public Date getCreateDate() {
      return createDate;
   }

   public void setCreateDate(Date createDate) {
      this.createDate = createDate;
   }

   public Date getModifyDate() {
      return modifyDate;
   }

   public void setModifyDate(Date modifyDate) {
      this.modifyDate = modifyDate;
   }

   public static final String BO = "BO";

   public static final String DATA_LIST = "DATA_LIST";

   public static final String LIST_NAME = "LIST_NAME";

   public static final String LIST_DESC = "LIST_DESC";

   public static final String CREATE_DATE = "CREATE_DATE";

   public static final String MODIFY_DATE = "MODIFY_DATE";

   @Override
   protected Serializable pkVal() {
      return this.bo;
   }


   public void setObjectSetBasicAttribute( Date date ){

     this.createDate=date;

     this.modifyDate=date;
   }

   @Override
   public String toString() {
      return "DataList{" +
         ", bo = " + bo +
         ", dataList = " + dataList +
         ", listName = " + listName +
         ", listDesc = " + listDesc +
         ", createDate = " + createDate +
         ", modifyDate = " + modifyDate +
         "}";
   }
}