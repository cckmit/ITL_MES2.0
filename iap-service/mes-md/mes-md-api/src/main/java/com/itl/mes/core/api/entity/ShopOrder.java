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
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 工单表
 * </p>
 *
 * @author space
 * @since 2019-06-18
 */

@Data
@TableName("m_shop_order")
@ApiModel(value="ShopOrder",description="工单表")
public class ShopOrder extends Model<ShopOrder> {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value="SO:SITE,SHOP_ORDER【PK】")
    @Length( max = 100 )
    @NotBlank
    @TableId(value = "BO", type = IdType.INPUT)
    private String bo;

    @ApiModelProperty(value="工厂【UK】")
    @Length( max = 32 )
    @TableField("SITE")
    private String site;

    @ApiModelProperty(value="工单号【UK】")
    @Length( max = 64 )
    @NotBlank
    @TableField("SHOP_ORDER")
    private String shopOrder;

    @ApiModelProperty(value="客户订单")
    @Length( max = 100 )
    @TableField("CUSTOMER_ORDER_BO")
    private String customerOrderBo;

    @ApiModelProperty(value="工单描述")
    @Length( max = 50 )
    @TableField("ORDER_DESC")
    private String orderDesc;

    @ApiModelProperty(value="工单状态")
    @Length( max = 100 )
    @NotBlank
    @TableField("STATE_BO")
    private String stateBo;

    @ApiModelProperty(value="是否允许超产(Y/N)")
    @Length( max = 1 )
    @NotBlank
    @Pattern( regexp = "^(Y|N)$" , message = "值必须为Y或者N" )
    @TableField("IS_OVERFULFILL")
    private String isOverfulfill;

    @ApiModelProperty(value="超产数量")
    @TableField("OVERFULFILL_QTY")
    private BigDecimal overfulfillQty;

    @ApiModelProperty(value="排产数量")
    @TableField("SCHEDUL_QTY")
    private BigDecimal schedulQty;

    @ApiModelProperty(value="完成数量")
    @TableField("COMPLETE_QTY")
    private BigDecimal completeQty;

    @ApiModelProperty(value="订单数量")
    @TableField("ORDER_QTY")
    @NotNull
    private BigDecimal orderQty;

    @ApiModelProperty(value="下达数量(SFC生成数量)")
    @TableField("RELEASE_QTY")
    private BigDecimal releaseQty;

    @ApiModelProperty(value="SFC报废数量")
    @TableField("SCRAP_QTY")
    private BigDecimal scrapQty;

    @ApiModelProperty(value="生产物料")
    @Length( max = 100 )
    @NotBlank
    @TableField("ITEM_BO")
    private String itemBo;

    @ApiModelProperty(value="计划产线")
    @Length( max = 100 )
    @TableField("PRODUCT_LINE_BO")
    private String productLineBo;

    @ApiModelProperty(value="计划工艺路线")
    @Length( max = 100 )
    @TableField("ROUTER_BO")
    private String routerBo;

    @ApiModelProperty(value="计划物料清单")
    @Length( max = 100 )
    @TableField("BOM_BO")
    private String bomBo;

    @ApiModelProperty(value="计划结束日期")
    @TableField("PLAN_END_DATE")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date planEndDate;

    @ApiModelProperty(value="计划开始日期")
    @TableField("PLAN_START_DATE")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date planStartDate;

    @ApiModelProperty(value="建档日期")
    @TableField("CREATE_DATE")
    private Date createDate;

    @ApiModelProperty(value="修改人")
    @Length( max = 32 )
    @TableField("CREATE_USER")
    private String createUser;

    @ApiModelProperty(value="修改人")
    @Length( max = 32 )
    @TableField("MODIFY_USER")
    private String modifyUser;

    @ApiModelProperty(value="修改日期")
    @TableField("MODIFY_DATE")
    private Date modifyDate;

    @ApiModelProperty(value="协商交期")
    @TableField("NEGOTIATION_TIME")
    private Date negotiationTime;

    @ApiModelProperty(value="固定交期")
    @TableField("FIXED_TIME")
    private Date fixedTime;

    @ApiModelProperty(value="紧急状态")
    @TableField("EMERGENCY_STATE")
    private String emergencyState;

    @ApiModelProperty(value="加急备注")
    @TableField("EMERGENCY_BZ")
    private String emergencyBz;

    @ApiModelProperty(value="订单交期")
    @TableField("ORDER_DELIVERY_TIME")
    private Date orderDeliveryTime;

    @ApiModelProperty(value="车间")
    @TableField("WORK_SHOP")
    private String workShop;

    @ApiModelProperty(value="订单类型")
    @TableField("SHOP_TYPE")
    private String shopType;

    @TableField(exist = false)
    private String itemDesc;

    @TableField(exist = false)
    private String processChar;

    @TableField(exist = false)
    private String colourSys;

    @TableField(exist = false)
    private String screwCombination;

    @TableField("SHOP_ORDER_CLASS")
    private String shopOrderClass;

    @TableField(exist = false)
    private String item;

    @TableField(exist = false)
    private String itemName;

    @TableField(exist = false)
    private String itemVersion;

    @ApiModelProperty(value="可下达数量")
    @TableField(exist = false)
    private BigDecimal releasable;

    @TableField(exist = false)
    private String router;

    @TableField(exist = false)
    private String routerName;

    @TableField(exist = false)
    private String version;

    @TableField(exist = false)
    private String shopOrderState;

    @ApiModelProperty(value="是否是ERP结案工单（默认为非结案：0，结案为1）")
    @TableField("IS_ERP_CLOSE_ORDER")
    private String isErpCloseOrder;

    @TableField("EPR_STOCK_QTY")
    private BigDecimal erpStockQty;

    @TableField("COMPLETE_SET_QTY")
    private BigDecimal completeSetQty;

    @TableField("START_TIME")
    private Date startTime;

    @TableField("END_TIME")
    private Date endTime;

    public String getItemVersion() {
        return itemVersion;
    }

    public void setItemVersion(String itemVersion) {
        this.itemVersion = itemVersion;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public BigDecimal getReleasable() {
        return releasable;
    }

    public void setReleasable(BigDecimal releasable) {
        this.releasable = releasable;
    }

    public String getRouter() {
        return router;
    }

    public void setRouter(String router) {
        this.router = router;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getShopOrderClass() {
        return shopOrderClass;
    }

    public void setShopOrderClass(String shopOrderClass) {
        this.shopOrderClass = shopOrderClass;
    }

    public String getColourSys() {
        return colourSys;
    }

    public void setColourSys(String colourSys) {
        this.colourSys = colourSys;
    }

    public String getProcessChar() {
        return processChar;
    }

    public void setProcessChar(String processChar) {
        this.processChar = processChar;
    }

    public String getScrewCombination() {
        return screwCombination;
    }

    public void setScrewCombination(String screwCombination) {
        this.screwCombination = screwCombination;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public Date getOrderDeliveryTime() {
        return orderDeliveryTime;
    }

    public void setOrderDeliveryTime(Date orderDeliveryTime) {
        this.orderDeliveryTime = orderDeliveryTime;
    }

    public String getEmergencyState() {
        return emergencyState;
    }

    public void setEmergencyState(String emergencyState) {
        this.emergencyState = emergencyState;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public String getEmergencyBz() {
        return emergencyBz;
    }

    public void setEmergencyBz(String emergencyBz) {
        this.emergencyBz = emergencyBz;
    }

    public Date getNegotiationTime() {
        return negotiationTime;
    }

    public void setNegotiationTime(Date negotiationTime) {
        this.negotiationTime = negotiationTime;
    }

    public Date getFixedTime() {
        return fixedTime;
    }

    public void setFixedTime(Date fixedTime) {
        this.fixedTime = fixedTime;
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

    public String getShopOrder() {
        return shopOrder;
    }

    public void setShopOrder(String shopOrder) {
        this.shopOrder = shopOrder;
    }

    public String getCustomerOrderBo() {
        return customerOrderBo;
    }

    public void setCustomerOrderBo(String customerOrderBo) {
        this.customerOrderBo = customerOrderBo;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }

    public String getStateBo() {
        return stateBo;
    }

    public void setStateBo(String stateBo) {
        this.stateBo = stateBo;
    }

    public String getIsOverfulfill() {
        return isOverfulfill;
    }

    public void setIsOverfulfill(String isOverfulfill) {
        this.isOverfulfill = isOverfulfill;
    }

    public BigDecimal getOverfulfillQty() {
        return overfulfillQty;
    }

    public void setOverfulfillQty(BigDecimal overfulfillQty) {
        this.overfulfillQty = overfulfillQty;
    }

    public BigDecimal getSchedulQty() {
        return schedulQty;
    }

    public void setSchedulQty(BigDecimal schedulQty) {
        this.schedulQty = schedulQty;
    }

    public BigDecimal getCompleteQty() {
        return completeQty;
    }

    public void setCompleteQty(BigDecimal completeQty) {
        this.completeQty = completeQty;
    }

    public BigDecimal getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(BigDecimal orderQty) {
        this.orderQty = orderQty;
    }

    public BigDecimal getReleaseQty() {
        return releaseQty;
    }

    public void setReleaseQty(BigDecimal releaseQty) {
        this.releaseQty = releaseQty;
    }

    public BigDecimal getScrapQty() {
        return scrapQty;
    }

    public void setScrapQty(BigDecimal scrapQty) {
        this.scrapQty = scrapQty;
    }

    public String getItemBo() {
        return itemBo;
    }

    public void setItemBo(String itemBo) {
        this.itemBo = itemBo;
    }

    public String getProductLineBo() {
        return productLineBo;
    }

    public void setProductLineBo(String productLineBo) {
        this.productLineBo = productLineBo;
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

    public Date getPlanEndDate() {
        return planEndDate;
    }

    public void setPlanEndDate(Date planEndDate) {
        this.planEndDate = planEndDate;
    }

    public Date getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanStartDate(Date planStartDate) {
        this.planStartDate = planStartDate;
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

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getWorkShop() {
        return workShop;
    }

    public void setWorkShop(String workShop) {
        this.workShop = workShop;
    }

    public String getIsErpCloseOrder() {
        return isErpCloseOrder;
    }

    public void setIsErpCloseOrder(String isErpCloseOrder) {
        this.isErpCloseOrder = isErpCloseOrder;
    }

    public BigDecimal getErpStockQty() {
        return erpStockQty;
    }

    public void setErpStockQty(BigDecimal erpStockQty) {
        this.erpStockQty = erpStockQty;
    }

    public static final String BO = "BO";

    public static final String SITE = "SITE";

    public static final String SHOP_ORDER = "SHOP_ORDER";

    public static final String CUSTOMER_ORDER_BO = "CUSTOMER_ORDER_BO";

    public static final String ORDER_DESC = "ORDER_DESC";

    public static final String STATE_BO = "STATE_BO";

    public static final String IS_OVERFULFILL = "IS_OVERFULFILL";

    public static final String OVERFULFILL_QTY = "OVERFULFILL_QTY";

    public static final String SCHEDUL_QTY = "SCHEDUL_QTY";

    public static final String COMPLETE_QTY = "COMPLETE_QTY";

    public static final String ORDER_QTY = "ORDER_QTY";

    public static final String RELEASE_QTY = "RELEASE_QTY";

    public static final String SCRAP_QTY = "SCRAP_QTY";

    public static final String ITEM_BO = "ITEM_BO";

    public static final String PRODUCT_LINE_BO = "PRODUCT_LINE_BO";

    public static final String ROUTER_BO = "ROUTER_BO";

    public static final String BOM_BO = "BOM_BO";

    public static final String PLAN_END_DATE = "PLAN_END_DATE";

    public static final String PLAN_START_DATE = "PLAN_START_DATE";

    public static final String CREATE_DATE = "CREATE_DATE";

    public static final String CREATE_USER = "CREATE_USER";

    public static final String MODIFY_USER = "MODIFY_USER";

    public static final String MODIFY_DATE = "MODIFY_DATE";

    public static final String FINISHED_STATE = "STATE:dongyin,503";

    public static final String CLOSE_STATE = "STATE:dongyin,502";

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
        return "ShopOrder{" +
                ", bo = " + bo +
                ", site = " + site +
                ", shopOrder = " + shopOrder +
                ", customerOrderBo = " + customerOrderBo +
                ", orderDesc = " + orderDesc +
                ", stateBo = " + stateBo +
                ", isOverfulfill = " + isOverfulfill +
                ", overfulfillQty = " + overfulfillQty +
                ", schedulQty = " + schedulQty +
                ", completeQty = " + completeQty +
                ", orderQty = " + orderQty +
                ", releaseQty = " + releaseQty +
                ", scrapQty = " + scrapQty +
                ", itemBo = " + itemBo +
                ", productLineBo = " + productLineBo +
                ", routerBo = " + routerBo +
                ", bomBo = " + bomBo +
                ", planEndDate = " + planEndDate +
                ", planStartDate = " + planStartDate +
                ", createDate = " + createDate +
                ", createUser = " + createUser +
                ", modifyUser = " + modifyUser +
                ", modifyDate = " + modifyDate +
                "}";
    }
}