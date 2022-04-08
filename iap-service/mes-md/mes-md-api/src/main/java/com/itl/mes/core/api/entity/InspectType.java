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
 * 检验类型维护
 * </p>
 *
 * @author lzh
 * @since 2019-08-28
 */
@TableName("z_inspect_type")
@ApiModel(value="InspectType",description="检验类型维护")
public class InspectType extends Model<InspectType> {

    private static final long serialVersionUID = 1L;


    @Excel( name="表BO", orderNum="16")
    @ApiModelProperty(value="IT:SITE,INSPECT_TYPE[PK]")
    @Length( max = 100 )
    @TableId(value = "BO", type = IdType.INPUT)
    private String bo;

    @Excel( name="SITE", orderNum="15")
    @ApiModelProperty(value="工厂")
    @Length( max = 32 )
    @TableField("SITE")
    private String site;

    @Excel( name="检验类型", orderNum="0")
    @ApiModelProperty(value="检验类型")
    @Length( max = 64 )
    @TableField("INSPECT_TYPE")
    private String inspectType;

    @Excel( name="检验类型名称", orderNum="1")
    @ApiModelProperty(value="检验类型名称")
    @Length( max = 64 )
    @TableField("INSPECT_TYPE_NAME")
    private String inspectTypeName;

    @Excel( name="检验类型描述", orderNum="2")
    @ApiModelProperty(value="检验类型描述")
    @Length( max = 200 )
    @TableField("INSPECT_TYPE_DESC")
    private String inspectTypeDesc;

    @Excel( name="状态", orderNum="3")
    @ApiModelProperty(value="状态（0：未启用，1：已启用）")
    @Length( max = 10 )
    @TableField("STATE")
    private String state;

    @Excel( name="车间是否必要", orderNum="4")
    @ApiModelProperty(value="车间是否必要(0:非，1:必)")
    @Length( max = 10 )
    @TableField("REQUIRED_WORK_SHOP")
    private String requiredWorkShop;

    @Excel( name="产线是否必要", orderNum="5")
    @ApiModelProperty(value="产线是否必要(0:非，1:必)")
    @Length( max = 10 )
    @TableField("REQUIRED_PRODUCT_LINE")
    private String requiredProductLine;

    @Excel( name="物料是否必要", orderNum="6")
    @ApiModelProperty(value="物料是否必要(0:非，1:必)")
    @Length( max = 10 )
    @TableField("REQUIRED_ITEM")
    private String requiredItem;

    @Excel( name="工序是否必要", orderNum="7")
    @ApiModelProperty(value="工序是否必要(0:非，1:必)")
    @Length( max = 10 )
    @TableField("REQUIRED_OPERATION")
    private String requiredOperation;

    @Excel( name="工位是否必要", orderNum="8")
    @ApiModelProperty(value="工位是否必要(0:非，1:必)")
    @Length( max = 10 )
    @TableField("REQUIRED_STATION")
    private String requiredStation;

    @Excel( name="工单是否必要", orderNum="9")
    @ApiModelProperty(value="工单是否必要(0:非，1:必)")
    @Length( max = 10 )
    @TableField("REQUIRED_SHOP_ORDER")
    private String requiredShopOrder;

    @Excel( name="产品条码是否必要", orderNum="10")
    @ApiModelProperty(value="产品条码是否必要(0:非，1:必)")
    @Length( max = 10 )
    @TableField("REQUIRED_SFC")
    private String requiredSfc;

    @Excel( name="创建人", orderNum="11")
    @ApiModelProperty(value="创建人")
    @Length( max = 32 )
    @TableField("CREATE_USER")
    private String createUser;

    @Excel( name="创建时间", orderNum="12",exportFormat = "yyyy-MM-dd",importFormat = "yyyy-MM-dd")
    @ApiModelProperty(value="创建时间")
    @TableField("CREATE_DATE")
    private Date createDate;

    @Excel( name="修改人", orderNum="13")
    @ApiModelProperty(value="修改人")
    @Length( max = 32 )
    @TableField("MODIFY_USER")
    private String modifyUser;

    @Excel( name="修改时间", orderNum="14",exportFormat = "yyyy-MM-dd hh:mm:ss")
    @ApiModelProperty(value="修改时间")
    @TableField("MODIFY_DATE")
    private Date modifyDate;

    public String getRequiredOperation() {
        return requiredOperation;
    }

    public void setRequiredOperation(String requiredOperation) {
        this.requiredOperation = requiredOperation;
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

    public String getInspectType() {
      return inspectType;
   }

    public void setInspectType(String inspectType) {
        this.inspectType = inspectType;
    }

    public String getInspectTypeName() {
      return inspectTypeName;
   }

    public void setInspectTypeName(String inspectTypeName) {
        this.inspectTypeName = inspectTypeName;
    }

    public String getInspectTypeDesc() {
      return inspectTypeDesc;
   }

    public void setInspectTypeDesc(String inspectTypeDesc) {
        this.inspectTypeDesc = inspectTypeDesc;
    }

    public String getState() {
      return state;
   }

    public void setState(String state) {
        this.state = state;
    }

    public String getRequiredWorkShop() {
      return requiredWorkShop;
   }

    public void setRequiredWorkShop(String requiredWorkShop) {
        this.requiredWorkShop = requiredWorkShop;
    }

    public String getRequiredProductLine() {
      return requiredProductLine;
   }

    public void setRequiredProductLine(String requiredProductLine) {
        this.requiredProductLine = requiredProductLine;
    }

    public String getRequiredItem() {
      return requiredItem;
   }

    public void setRequiredItem(String requiredItem) {
        this.requiredItem = requiredItem;
    }

    public String getRequiredStation() {
      return requiredStation;
   }

    public void setRequiredStation(String requiredStation) {
        this.requiredStation = requiredStation;
    }

    public String getRequiredShopOrder() {
      return requiredShopOrder;
   }

    public void setRequiredShopOrder(String requiredShopOrder) {
        this.requiredShopOrder = requiredShopOrder;
    }

    public String getRequiredSfc() {
      return requiredSfc;
   }

    public void setRequiredSfc(String requiredSfc) {
        this.requiredSfc = requiredSfc;
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

    public static final String INSPECT_TYPE = "INSPECT_TYPE";

    public static final String INSPECT_TYPE_NAME = "INSPECT_TYPE_NAME";

    public static final String INSPECT_TYPE_DESC = "INSPECT_TYPE_DESC";

    public static final String STATE = "STATE";

    public static final String REQUIRED_WORK_SHOP = "REQUIRED_WORK_SHOP";

    public static final String REQUIRED_PRODUCT_LINE = "REQUIRED_PRODUCT_LINE";

    public static final String REQUIRED_ITEM = "REQUIRED_ITEM";

    public static final String REQUIRED_STATION = "REQUIRED_STATION";

    public static final String REQUIRED_SHOP_ORDER = "REQUIRED_SHOP_ORDER";

    public static final String REQUIRED_SFC = "REQUIRED_SFC";

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
        return "InspectType{" +
                "bo='" + bo + '\'' +
                ", site='" + site + '\'' +
                ", inspectType='" + inspectType + '\'' +
                ", inspectTypeName='" + inspectTypeName + '\'' +
                ", inspectTypeDesc='" + inspectTypeDesc + '\'' +
                ", state='" + state + '\'' +
                ", requiredWorkShop='" + requiredWorkShop + '\'' +
                ", requiredProductLine='" + requiredProductLine + '\'' +
                ", requiredItem='" + requiredItem + '\'' +
                ", requiredOperation='" + requiredOperation + '\'' +
                ", requiredStation='" + requiredStation + '\'' +
                ", requiredShopOrder='" + requiredShopOrder + '\'' +
                ", requiredSfc='" + requiredSfc + '\'' +
                ", createUser='" + createUser + '\'' +
                ", createDate=" + createDate +
                ", modifyUser='" + modifyUser + '\'' +
                ", modifyDate=" + modifyDate +
                '}';
    }
}