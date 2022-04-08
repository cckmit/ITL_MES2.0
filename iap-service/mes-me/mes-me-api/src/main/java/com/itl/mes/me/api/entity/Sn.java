package com.itl.mes.me.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 条码信息表
 *
 * @author cuichonghe
 * 2020-12-25 12:36:32
 */
@Data
@TableName("z_sn")
@ApiModel(value = "z_sn", description = "条码信息表")
public class Sn implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * SN:SITE,SN【PK】
     */
    @TableId
    @ApiModelProperty(value = "SN:SITE,SN【PK】")
    private String bo;
    /**
     * 站点【UK】
     */
    @ApiModelProperty(value = "站点【UK】")
    private String site;
    /**
     * 条码【UK】
     */
    @ApiModelProperty(value = "条码【UK】")
    private String sn;
    /**
     * 原条码【UK】
     */
    @TableField("OLD_SN")
    @ApiModelProperty(value = "原条码【UK】")
    private String oldSn;
    /**
     * 补码状态(Y:补码;N:正常码)
     */
    @TableField("COMPLEMENT_CODE_STATE")
    @ApiModelProperty(value = "补码状态(Y:补码;N:正常码)")
    private String complementCodeState;
    /**
     * 物料【UK】
     */
    @TableField("ITEM_BO")
    @ApiModelProperty(value = "物料【UK】")
    private String itemBo;
    /**
     * 原始物料BO
     */
    @TableField("ORIGINAL_ITEM_BO")
    @ApiModelProperty(value = "原始物料BO")
    private String originalItemBo;
    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private BigDecimal qty;
    /**
     * 工单号
     */
    @TableField("SHOP_ORDER")
    @ApiModelProperty(value = "工单号")
    private String shopOrder;
    /**
     * 任务号
     */
    @TableField("TASK_NO")
    @ApiModelProperty(value = "任务号")
    private String taskNo;
    /**
     * 物料清单
     */
    @TableField("BOM_BO")
    @ApiModelProperty(value = "物料清单")
    private String bomBo;
    /**
     * 成型工单
     */
    @TableField("SHAP_ORDER_BO")
    @ApiModelProperty(value = "成型工单")
    private String shopOrderBo;

    /**
     * 喷釉工单
     */
    @TableField("PAINT_ORDER_BO")
    @ApiModelProperty(value = "喷釉工单")
    private String paintOrderBo;

    /**
     * 烧成工单
     */
    @TableField("FIRE_ORDER_BO")
    @ApiModelProperty(value = "烧成工单")
    private String fireOrderBo;

    /**
     * 包装工单
     */
    @TableField("PACK_ORDER_BO")
    @ApiModelProperty(value = "包装工单")
    private String packOrderBo;

    /**
     * 生产线
     */
    @TableField("PRODUCT_LINE_BO")
    @ApiModelProperty(value = "生产线")
    private String productLineBo;

    /**
     * 状态（401新建402出烘干室绑车403青坯入库 404青坯出库 405修检扫描 406擦绺扫描 407 喷釉扫码408白坯入库409白坯出库410装窑扫码411烧成入窑412烧成开窑413成检扫描414回烧接收415回烧装窑416回烧出窑417装箱扫描 421报废422保留423青坯返坯 424白坯返坯 425破损 426 冻结418回烧补瓷 419青坯补码 420成检补码）
     */
    @ApiModelProperty(value = "状态（401新建402出烘干室绑车403青坯入库 404青坯出库 405修检扫描 406擦绺扫描 407 喷釉扫码408白坯入库409白坯出库410装窑扫码411烧成入窑412烧成开窑413成检扫描414回烧接收415回烧装窑416回烧出窑417装箱扫描 421报废422保留423青坯返坯 424白坯返坯 425破损 426 冻结418回烧补瓷 419青坯补码 420成检补码）")
    private String state;

    /**
     * 投入数量
     */
    @TableField("INPUT_QTY")
    @ApiModelProperty(value = "投入数量")
    private BigDecimal inputQty;

    /**
     * 产出数量
     */
    @TableField("OUT_QTY")
    @ApiModelProperty(value = "产出数量")
    private BigDecimal outQty;

    /**
     * 报废数量
     */
    @TableField("SCRAP_QTY")
    @ApiModelProperty(value = "报废数量")
    private BigDecimal scrapQty;

    /**
     * 最近过站时间
     */
    @TableField("LATELY_PASS_DATE")
    @ApiModelProperty(value = "最近过站时间")
    private Date latelyPassDate;

    /**
     * 最近过站工序
     */
    @TableField("LATELY_PASS_OPERATION")
    @ApiModelProperty(value = "最近过站工序")
    private String latelyPassOperation;

    /**
     * 最近过站工位
     */
    @TableField("LATELY_PASS_STATION")
    @ApiModelProperty(value = "最近过站工位")
    private String latelyPassStation;

    /**
     * 完成时间
     */
    @TableField("COMPLETE_DATE")
    @ApiModelProperty(value = "完成时间")
    private String completeDate;

    /**
     * 物料类型
     */
    @TableField("ITEM_TYPE")
    @ApiModelProperty(value = "物料类型")
    private String itemType;

    /**
     * 当前最大流水号
     */
    @TableField("MAX_SERIAL_NUMBER")
    @ApiModelProperty(value = "当前最大流水号")
    private Integer maxSerialNumber;

    /**
     * 建档人
     */
    @TableField("CREATE_USER")
    @ApiModelProperty(value = "建档人")
    private String createUser;

    /**
     * 建档日期
     */
    @TableField("CREATE_DATE")
    @ApiModelProperty(value = "建档日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    private Date createDate;

    /**
     * 修改人
     */
    @TableField("MODIFY_USER")
    @ApiModelProperty(value = "修改人")
    private String modifyUser;

    /**
     * 修改时间
     */
    @TableField("MODIFY_DATE")
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    private Date modifyDate;

    /**
     * Y 精坯 N毛坯
     */
    @TableField("IS_BOUTIQUE")
    @ApiModelProperty(value = "Y 精坯 N毛坯")
    private String isBoutique;

    @TableField("GET_USER")
    @ApiModelProperty(value = "领用人")
    private String getUser;

    @TableField("GET_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    @ApiModelProperty(value = "领用时间")
    private Date getTime;

    @TableField("DEADLINE")
    @ApiModelProperty(value = "条码截止期限")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date deadline;

    @ApiModelProperty(value = "车间")
    @TableField(exist = false)
    private String workShop;

    @TableField(exist = false)
    @ApiModelProperty(value = "物料")
    private String itemName;

    @TableField(exist = false)
    @ApiModelProperty(value = "产线")
    private String productLine;

    @ApiModelProperty(value = "车间bo")
    @TableField("WORK_SHOP_BO")
    private String workShopBo;

}

