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
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 条码信息表
 * </p>
 *
 * @author space
 * @since 2019-10-25
 */
@TableName("z_sn")
@ApiModel(value="Sn",description="条码信息表")
public class Sn extends Model<Sn> {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value="SN:SITE,SN【PK】")
    @Length( max = 100 )
    @TableId(value = "BO", type = IdType.INPUT)
    private String bo;

    @ApiModelProperty(value="站点【UK】")
    @Length( max = 32 )
    @TableField("SITE")
    private String site;

    @ApiModelProperty(value="条码【UK】")
    @Length( max = 64 )
    @TableField("SN")
    private String sn;

    @ApiModelProperty(value="原条码【UK】")
    @Length( max = 64 )
    @TableField("OLD_SN")
    private String oldSn;

    @ApiModelProperty(value="补码状态(Y:补码;N:正常码)")
    @Length( max = 1 )
    @TableField("COMPLEMENT_CODE_STATE")
    private String complementCodeState;

    @ApiModelProperty(value="物料【UK】")
    @Length( max = 100 )
    @TableField("ITEM_BO")
    private String itemBo;

    @ApiModelProperty(value="原始物料BO")
    @Length( max = 100 )
    @TableField("ORIGINAL_ITEM_BO")
    private String originalItemBo;

    @ApiModelProperty(value="数量")
    @TableField("QTY")
    private BigDecimal qty;

    @ApiModelProperty(value="工单号")
    @Length( max = 64 )
    @TableField("SHOP_ORDER")
    private String shopOrder;

    @ApiModelProperty(value="任务号")
    @Length( max = 64 )
    @TableField("TASK_NO")
    private String taskNo;

    @ApiModelProperty(value="物料清单")
    @Length( max = 100 )
    @TableField("BOM_BO")
    private String bomBo;

    @ApiModelProperty(value="成型工单")
    @Length( max = 64 )
    @TableField("SHAP_ORDER_BO")
    private String shapOrderBo;

    @ApiModelProperty(value="喷釉工单")
    @Length( max = 64 )
    @TableField("PAINT_ORDER_BO")
    private String paintOrderBo;

    @ApiModelProperty(value="烧成工单")
    @Length( max = 64 )
    @TableField("FIRE_ORDER_BO")
    private String fireOrderBo;

    @ApiModelProperty(value="包装工单")
    @Length( max = 64 )
    @TableField("PACK_ORDER_BO")
    private String packOrderBo;

    @ApiModelProperty(value="生产线")
    @Length( max = 100 )
    @TableField("PRODUCT_LINE_BO")
    private String productLineBo;

    @ApiModelProperty(value="状态（401新建402出烘干室绑车403青坯入库 404青坯出库 405修检扫描 406擦绺扫描 407 喷釉扫码408白坯入库409白坯出库410装窑扫码411烧成入窑412烧成开窑413成检扫描414回烧接收415回烧装窑416回烧出窑417装箱扫描 421报废422保留423青坯返坯 424白坯返坯 425破损 426 冻结418回烧补瓷）")
    @Length( max = 10 )
    @TableField("STATE")
    private String state;

    @ApiModelProperty(value="Y 精坯 N毛坯")
    @Length( max = 1 )
    @TableField("IS_BOUTIQUE")
    private String isBoutique;

    @ApiModelProperty(value="投入数量")
    @TableField("INPUT_QTY")
    private BigDecimal inputQty;

    @ApiModelProperty(value="产出数量")
    @TableField("OUT_QTY")
    private BigDecimal outQty;

    @ApiModelProperty(value="报废数量")
    @TableField("SCRAP_QTY")
    private BigDecimal scrapQty;

    @ApiModelProperty(value="最近过站时间")
    @TableField("LATELY_PASS_DATE")
    private Date latelyPassDate;

    @ApiModelProperty(value="最近过站工序")
    @Length( max = 64 )
    @TableField("LATELY_PASS_OPERATION")
    private String latelyPassOperation;

    @ApiModelProperty(value="最近过站工位")
    @Length( max = 64 )
    @TableField("LATELY_PASS_STATION")
    private String latelyPassStation;

    @ApiModelProperty(value="完成时间")
    @Length( max = 14 )
    @TableField("COMPLETE_DATE")
    private String completeDate;

    @ApiModelProperty(value="物料类型")
    @Length( max = 10 )
    @TableField("ITEM_TYPE")
    private String itemType;

    @ApiModelProperty(value="当前最大流水号")
    @TableField("MAX_SERIAL_NUMBER")
    private Integer maxSerialNumber;

    @ApiModelProperty(value="建档人")
    @Length( max = 32 )
    @TableField("CREATE_USER")
    private String createUser;

    @ApiModelProperty(value="建档日期")
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

    public String getSn() {
      return sn;
   }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getOldSn() {
      return oldSn;
   }

    public void setOldSn(String oldSn) {
        this.oldSn = oldSn;
    }

    public String getComplementCodeState() {
      return complementCodeState;
   }

    public void setComplementCodeState(String complementCodeState) {
        this.complementCodeState = complementCodeState;
    }

    public String getItemBo() {
      return itemBo;
   }

    public void setItemBo(String itemBo) {
        this.itemBo = itemBo;
    }

    public String getOriginalItemBo() {
      return originalItemBo;
   }

    public void setOriginalItemBo(String originalItemBo) {
        this.originalItemBo = originalItemBo;
    }

    public BigDecimal getQty() {
      return qty;
   }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getShopOrder() {
      return shopOrder;
   }

    public void setShopOrder(String shopOrder) {
        this.shopOrder = shopOrder;
    }

    public String getTaskNo() {
      return taskNo;
   }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getBomBo() {
      return bomBo;
   }

    public void setBomBo(String bomBo) {
        this.bomBo = bomBo;
    }

    public String getShapOrderBo() {
      return shapOrderBo;
   }

    public void setShapOrderBo(String shapOrderBo) {
        this.shapOrderBo = shapOrderBo;
    }

    public String getPaintOrderBo() {
      return paintOrderBo;
   }

    public void setPaintOrderBo(String paintOrderBo) {
        this.paintOrderBo = paintOrderBo;
    }

    public String getFireOrderBo() {
      return fireOrderBo;
   }

    public void setFireOrderBo(String fireOrderBo) {
        this.fireOrderBo = fireOrderBo;
    }

    public String getPackOrderBo() {
      return packOrderBo;
   }

    public void setPackOrderBo(String packOrderBo) {
        this.packOrderBo = packOrderBo;
    }

    public String getProductLineBo() {
      return productLineBo;
   }

    public void setProductLineBo(String productLineBo) {
        this.productLineBo = productLineBo;
    }

    public String getState() {
      return state;
   }

    public void setState(String state) {
        this.state = state;
    }

    public String getIsBoutique() {
      return isBoutique;
   }

    public void setIsBoutique(String isBoutique) {
        this.isBoutique = isBoutique;
    }

    public BigDecimal getInputQty() {
      return inputQty;
   }

    public void setInputQty(BigDecimal inputQty) {
        this.inputQty = inputQty;
    }

    public BigDecimal getOutQty() {
      return outQty;
   }

    public void setOutQty(BigDecimal outQty) {
        this.outQty = outQty;
    }

    public BigDecimal getScrapQty() {
      return scrapQty;
   }

    public void setScrapQty(BigDecimal scrapQty) {
        this.scrapQty = scrapQty;
    }

    public Date getLatelyPassDate() {
      return latelyPassDate;
   }

    public void setLatelyPassDate(Date latelyPassDate) {
        this.latelyPassDate = latelyPassDate;
    }

    public String getLatelyPassOperation() {
      return latelyPassOperation;
   }

    public void setLatelyPassOperation(String latelyPassOperation) {
        this.latelyPassOperation = latelyPassOperation;
    }

    public String getLatelyPassStation() {
      return latelyPassStation;
   }

    public void setLatelyPassStation(String latelyPassStation) {
        this.latelyPassStation = latelyPassStation;
    }

    public String getCompleteDate() {
      return completeDate;
   }

    public void setCompleteDate(String completeDate) {
        this.completeDate = completeDate;
    }

    public String getItemType() {
      return itemType;
   }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Integer getMaxSerialNumber() {
      return maxSerialNumber;
   }

    public void setMaxSerialNumber(Integer maxSerialNumber) {
        this.maxSerialNumber = maxSerialNumber;
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

    public static final String SN = "SN";

    public static final String OLD_SN = "OLD_SN";

    public static final String COMPLEMENT_CODE_STATE = "COMPLEMENT_CODE_STATE";

    public static final String ITEM_BO = "ITEM_BO";

    public static final String ORIGINAL_ITEM_BO = "ORIGINAL_ITEM_BO";

    public static final String QTY = "QTY";

    public static final String SHOP_ORDER = "SHOP_ORDER";

    public static final String TASK_NO = "TASK_NO";

    public static final String BOM_BO = "BOM_BO";

    public static final String SHAP_ORDER_BO = "SHAP_ORDER_BO";

    public static final String PAINT_ORDER_BO = "PAINT_ORDER_BO";

    public static final String FIRE_ORDER_BO = "FIRE_ORDER_BO";

    public static final String PACK_ORDER_BO = "PACK_ORDER_BO";

    public static final String PRODUCT_LINE_BO = "PRODUCT_LINE_BO";

    public static final String STATE = "STATE";

    public static final String IS_BOUTIQUE = "IS_BOUTIQUE";

    public static final String INPUT_QTY = "INPUT_QTY";

    public static final String OUT_QTY = "OUT_QTY";

    public static final String SCRAP_QTY = "SCRAP_QTY";

    public static final String LATELY_PASS_DATE = "LATELY_PASS_DATE";

    public static final String LATELY_PASS_OPERATION = "LATELY_PASS_OPERATION";

    public static final String LATELY_PASS_STATION = "LATELY_PASS_STATION";

    public static final String COMPLETE_DATE = "COMPLETE_DATE";

    public static final String ITEM_TYPE = "ITEM_TYPE";

    public static final String MAX_SERIAL_NUMBER = "MAX_SERIAL_NUMBER";

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
        return "Sn{" +
            ", bo = " + bo +
            ", site = " + site +
            ", sn = " + sn +
            ", oldSn = " + oldSn +
            ", complementCodeState = " + complementCodeState +
            ", itemBo = " + itemBo +
            ", originalItemBo = " + originalItemBo +
            ", qty = " + qty +
            ", shopOrder = " + shopOrder +
            ", taskNo = " + taskNo +
            ", bomBo = " + bomBo +
            ", shapOrderBo = " + shapOrderBo +
            ", paintOrderBo = " + paintOrderBo +
            ", fireOrderBo = " + fireOrderBo +
            ", packOrderBo = " + packOrderBo +
            ", productLineBo = " + productLineBo +
            ", state = " + state +
            ", isBoutique = " + isBoutique +
            ", inputQty = " + inputQty +
            ", outQty = " + outQty +
            ", scrapQty = " + scrapQty +
            ", latelyPassDate = " + latelyPassDate +
            ", latelyPassOperation = " + latelyPassOperation +
            ", latelyPassStation = " + latelyPassStation +
            ", completeDate = " + completeDate +
            ", itemType = " + itemType +
            ", maxSerialNumber = " + maxSerialNumber +
            ", createUser = " + createUser +
            ", createDate = " + createDate +
            ", modifyUser = " + modifyUser +
            ", modifyDate = " + modifyDate +
        "}";
    }
}
