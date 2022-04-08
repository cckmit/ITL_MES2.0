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
 * SN日志表
 * </p>
 *
 * @author space
 * @since 2019-09-25
 */
@TableName("z_sn_log")
@ApiModel(value="SnLog",description="SN日志表")
public class SnLog extends Model<SnLog> {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value="idWorker")
    @Length( max = 100 )
    @TableId(value = "BO", type = IdType.INPUT)
    private String bo;

    @ApiModelProperty(value="工厂")
    @Length( max = 32 )
    @TableField("SITE")
    private String site;

    @ApiModelProperty(value="物料编码")
    @Length( max = 64 )
    @TableField("ITEM")
    private String item;

    @ApiModelProperty(value="物料大类")
    @Length( max = 100 )
    @TableField("MATERIAL_TYPE")
    private String materialType;

    @ApiModelProperty(value="计划编号(工单号)")
    @Length( max = 64 )
    @TableField("SHOP_ORDER")
    private String shopOrder;

    @ApiModelProperty(value="生成数量")
    @TableField("CREATE_QUANTITY")
    private Integer createQuantity;

    @ApiModelProperty(value="开始号段")
    @Length( max = 100 )
    @TableField("START_NUMBER")
    private String startNumber;

    @ApiModelProperty(value="结束号段")
    @Length( max = 100 )
    @TableField("END_NUMBER")
    private String endNumber;

    @ApiModelProperty(value="建档人")
    @Length( max = 32 )
    @TableField("CREATE_USER")
    private String createUser;

    @ApiModelProperty(value="建档日期")
    @TableField("CREATE_DATE")
    private Date createDate;


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

    public String getItem() {
      return item;
   }

    public void setItem(String item) {
        this.item = item;
    }

    public String getMaterialType() {
      return materialType;
   }

    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    public String getShopOrder() {
      return shopOrder;
   }

    public void setShopOrder(String shopOrder) {
        this.shopOrder = shopOrder;
    }

    public Integer getCreateQuantity() {
      return createQuantity;
   }

    public void setCreateQuantity(Integer createQuantity) {
        this.createQuantity = createQuantity;
    }

    public String getStartNumber() {
      return startNumber;
   }

    public void setStartNumber(String startNumber) {
        this.startNumber = startNumber;
    }

    public String getEndNumber() {
      return endNumber;
   }

    public void setEndNumber(String endNumber) {
        this.endNumber = endNumber;
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

    public static final String BO = "BO";

    public static final String SITE = "SITE";

    public static final String ITEM = "ITEM";

    public static final String MATERIAL_TYPE = "MATERIAL_TYPE";

    public static final String SHOP_ORDER = "SHOP_ORDER";

    public static final String CREATE_QUANTITY = "CREATE_QUANTITY";

    public static final String START_NUMBER = "START_NUMBER";

    public static final String END_NUMBER = "END_NUMBER";

    public static final String CREATE_USER = "CREATE_USER";

    public static final String CREATE_DATE = "CREATE_DATE";

    @Override
    protected Serializable pkVal() {
        return this.bo;
    }


    public void setObjectSetBasicAttribute( String userId,Date date ){
        this.createUser=userId;
        this.createDate=date;
   }

    @Override
    public String toString() {
        return "SnLog{" +
            ", bo = " + bo +
            ", site = " + site +
            ", item = " + item +
            ", materialType = " + materialType +
            ", shopOrder = " + shopOrder +
            ", createQuantity = " + createQuantity +
            ", startNumber = " + startNumber +
            ", endNumber = " + endNumber +
            ", createUser = " + createUser +
            ", createDate = " + createDate +
        "}";
    }
}