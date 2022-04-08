package com.itl.mes.core.api.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 物料表
 * </p>
 *
 * @author space
 * @since 2019-05-31
 */
@TableName("m_item")
@ApiModel(value="Item",description="物料表")
@Data
public class Item extends Model<Item> {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value="CITEM:SITE,ITEM,VERSION【PK】")
    @Length( max = 100 )
    @NotBlank
    @TableId(value = "BO", type = IdType.INPUT)
    private String bo;

    @ApiModelProperty(value="工厂")
    @Length( max = 32 )
    @NotBlank
    @TableField("SITE")
    @Excel( name="工厂", orderNum="0" )
    private String site;

    @ApiModelProperty(value="物料编码")
    @Length( max = 64 )
    @NotBlank
    @TableField("ITEM")
    @Excel( name="物料编码", orderNum="1" )
    private String item;

    @ApiModelProperty(value="版本")
    @Length( max = 3 )
    @NotBlank
    @TableField("VERSION")
    @Excel( name="版本", orderNum="2" )
    private String version;

    @ApiModelProperty(value="是否为当前版本,Y:是当前版本、N:非当前版本")
    @Length( max = 1 )
    @NotBlank
    @Pattern( regexp = "^(Y|N)$" , message = "值必须为Y或者N" )
    @TableField("IS_CURRENT_VERSION")
    @Excel( name="是否为当前版本", orderNum="3" )
    private String isCurrentVersion;

    @ApiModelProperty(value="物料名称")
    @Length( max = 100 )
    @TableField("ITEM_NAME")
    @Excel( name="物料名称", orderNum="4" )
    private String itemName;

    @ApiModelProperty(value="物料描述")
    @Length( max = 200 )
    @TableField("ITEM_DESC")
    @Excel( name="物料描述", orderNum="5" )
    private String itemDesc;

    @ApiModelProperty(value="物料单位")
    @Length( max = 32 )
    @TableField("ITEM_UNIT")
    @Excel( name="物料单位", orderNum="6" )
    private String itemUnit;

    @ApiModelProperty(value="物料状态【M_STATUS的BO，STATUS_GROUP:ITEM】")
    @Length( max = 100 )
    @NotBlank
    @TableField("ITEM_STATE_BO")
    @Excel( name="物料状态", orderNum="7" )
    private String itemStateBo;

    @ApiModelProperty(value="物料类型")
    @Length( max = 32 )
    @TableField("ITEM_TYPE")
    @NotBlank
    @Excel( name="物料类型", orderNum="8" )
    private String itemType;

    @ApiModelProperty(value="工艺路线【M_ROUTER的BO】")
    @TableField("ROUTER_BO")
    @Excel( name="工艺路线", orderNum="9" )
    private String routerBo;

    @ApiModelProperty(value="物料清单【M_BOM的BO】")
    @TableField("BOM_BO")
    @Excel( name="物料清单", orderNum="10" )
    private String bomBo;

    @ApiModelProperty(value="默认仓库BO")
    @TableField("WAREHOUSE_BO")
    @Excel( name="默认仓库", orderNum="11" )
    private String warehouseBo;

    @ApiModelProperty(value="批量数")
    @TableField("LOT_SIZE")
    @NotNull
    @Excel( name="批量数", orderNum="12" )
    private BigDecimal lotSize;

    @ApiModelProperty(value="建档日期")
    @TableField("CREATE_DATE")
    @NotNull
    private Date createDate;

    @ApiModelProperty(value="建档人")
    @Length( max = 32 )
    @NotBlank
    @TableField("CREATE_USER")
    private String createUser;

    @ApiModelProperty(value="修改日期")
    @TableField("MODIFY_DATE")
    private Date modifyDate;

    @ApiModelProperty(value="修改人")
    @Length( max = 32 )
    @TableField("MODIFY_USER")
    private String modifyUser;

    @ApiModelProperty(value="工艺名称")
    @TableField("ROUTER_NAME")
    private String routerName;

    @ApiModelProperty(value="工艺版本")
    @TableField("ROUTER_VERSION")
    private String routerVersion;

    @ApiModelProperty("图号")
    @TableField("DRAWING_NO")
    private String drawingNo;

    @TableField(exist = false)
    private String operationOrderQty;

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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemUnit() {
        return itemUnit;
    }

    public void setItemUnit(String itemUnit) {
        this.itemUnit = itemUnit;
    }

    public String getItemStateBo() {
        return itemStateBo;
    }

    public void setItemStateBo(String itemStateBo) {
        this.itemStateBo = itemStateBo;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getRouterBo() {
        return routerBo;
    }

    public void setRouterBo(String routerBo) {
        this.routerBo = routerBo;
    }

    public String getBomBo() {
        return bomBo;
    }

    public void setBomBo(String bomBo) {
        this.bomBo = bomBo;
    }

    public String getWarehouseBo() {
        return warehouseBo;
    }

    public void setWarehouseBo(String warehouseBo) {
        this.warehouseBo = warehouseBo;
    }

    public BigDecimal getLotSize() {
        return lotSize;
    }

    public void setLotSize(BigDecimal lotSize) {
        this.lotSize = lotSize;
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

    public String getRouterName() {
        return routerName;
    }

    public void setRouterName(String routerName) {
        this.routerName = routerName;
    }

    public String getRouterVersion() {
        return routerVersion;
    }

    public void setRouterVersion(String routerVersion) {
        this.routerVersion = routerVersion;
    }

    public static final String BO = "BO";

    public static final String SITE = "SITE";

    public static final String ITEM = "ITEM";

    public static final String VERSION = "VERSION";

    public static final String IS_CURRENT_VERSION = "IS_CURRENT_VERSION";

    public static final String ITEM_NAME = "ITEM_NAME";

    public static final String ITEM_DESC = "ITEM_DESC";

    public static final String ITEM_UNIT = "ITEM_UNIT";

    public static final String ITEM_STATE_BO = "ITEM_STATE_BO";

    public static final String ITEM_TYPE = "ITEM_TYPE";

    public static final String ROUTER_BO = "ROUTER_BO";

    public static final String BOM_BO = "BOM_BO";

    public static final String WAREHOUSE_BO = "WAREHOUSE_BO";

    public static final String LOT_SIZE = "LOT_SIZE";

    public static final String CREATE_DATE = "CREATE_DATE";

    public static final String CREATE_USER = "CREATE_USER";

    public static final String MODIFY_DATE = "MODIFY_DATE";

    public static final String MODIFY_USER = "MODIFY_USER";

    public String getOperationOrderQty() {
        return operationOrderQty;
    }

    public void setOperationOrderQty(String operationOrderQty) {
        this.operationOrderQty = operationOrderQty;
    }

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
        return "Item{" +
                "bo='" + bo + '\'' +
                ", site='" + site + '\'' +
                ", item='" + item + '\'' +
                ", version='" + version + '\'' +
                ", isCurrentVersion='" + isCurrentVersion + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemDesc='" + itemDesc + '\'' +
                ", itemUnit='" + itemUnit + '\'' +
                ", itemStateBo='" + itemStateBo + '\'' +
                ", itemType='" + itemType + '\'' +
                ", routerBo='" + routerBo + '\'' +
                ", bomBo='" + bomBo + '\'' +
                ", warehouseBo='" + warehouseBo + '\'' +
                ", lotSize=" + lotSize +
                ", createDate=" + createDate +
                ", createUser='" + createUser + '\'' +
                ", modifyDate=" + modifyDate +
                ", modifyUser='" + modifyUser + '\'' +
                ", routerName='" + routerName + '\'' +
                ", routerVersion='" + routerVersion + '\'' +
                ", operationOrderQty='" + operationOrderQty + '\'' +
                '}';
    }
}