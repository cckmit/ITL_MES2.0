package com.itl.mes.me.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 车间作业控制信息表
 *
 * @author cuichonghe
 * @date 2021-01-25 14:43:35
 */
@Data
@TableName("me_sfc")
@ApiModel(value = "me_sfc", description = "comments")
@Accessors(chain = true)
public class MeSfc implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * SFC:SITE,SFC【PK】
	 */
	@TableId(value = "BO", type = IdType.INPUT)
	@ApiModelProperty(value = "SFC:SITE,SFC【PK】")
	private String bo;
	/**
	 * 工厂【UK】
	 */
	@ApiModelProperty(value = "工厂【UK】")
	@TableField("SITE")
	private String site;
	/**
	 * SN【UK】
	 */
	@ApiModelProperty(value = "SN【UK】")
	@TableField("SFC")
	private String sfc;
	/**
	 * 排程【UK】
	 */
	@ApiModelProperty(value = "排程【UK】")
	@TableField("SCHEDULE_BO")
	private String scheduleBo;
	/**
	 * 工单
	 */
	@ApiModelProperty(value = "工单")
	@TableField("SHOP_ORDER_BO")
	private String shopOrderBo;
	/**
	 * 车间
	 */
	@ApiModelProperty(value = "车间")
	@TableField("WORK_SHOP_BO")
	private String workShopBo;
	/**
	 * 产线
	 */
	@ApiModelProperty(value = "产线")
	@TableField("PRODUCT_LINE_BO")
	private String productLineBo;
	/**
	 * 当前工序
	 */
	@ApiModelProperty(value = "当前工序")
	@TableField("OPERATION_BO")
	private String operationBo;
	/**
	 * 当前工位
	 */
	@ApiModelProperty(value = "当前工位")
	@TableField("STATION_BO")
	private String stationBo;
	/**
	 * 当前设备
	 */
	@ApiModelProperty(value = "当前设备")
	@TableField("DEVICE_BO")
	private String deviceBo;
	/**
	 * 当前工号
	 */
	@ApiModelProperty(value = "当前工号")
	@TableField("USER_BO")
	private String userBo;
	/**
	 * 班次
	 */
	@ApiModelProperty(value = "班次")
	@TableField("SHIT_BO")
	private String shitBo;
	/**
	 * 班组
	 */
	@ApiModelProperty(value = "班组")
	@TableField("TEAM_BO")
	private String teamBo;
	/**
	 * 物料【UK】
	 */
	@ApiModelProperty(value = "物料【UK】")
	@TableField("ITEM_BO")
	private String itemBo;
	/**
	 * 物料清单
	 */
	@ApiModelProperty(value = "物料清单")
	@TableField("BOM_BO")
	private String bomBo;
	/**
	 * SFC工艺路线
	 */
	@ApiModelProperty(value = "SFC工艺路线")
	@TableField("SFC_ROUTER_BO")
	private String sfcRouterBo;
	/**
	 * SFC工序步骤
	 */
	@ApiModelProperty(value = "SFC工序步骤")
	@TableField("SFC_STEP_ID")
	private String sfcStepId;
	/**
	 * 父SFC
	 */
	@ApiModelProperty(value = "父SFC")
	@TableField("PARENT_SFC_BO")
	private String parentSfcBo;
	/**
	 * 状态
	 */
	@ApiModelProperty(value = "状态")
	@TableField("STATE")
	private String state;
	/**
	 * 当前工序进站时间
	 */
	@ApiModelProperty(value = "当前工序进站时间")
	@TableField("IN_TIME")
	private Date inTime;
	/**
	 * 是否批次处理(false-否,true-是)
	 */
	@ApiModelProperty(value = "是否批次处理(false-否,true-是)")
	@TableField("IS_BATCH")
	private Boolean isBatch;
	/**
	 * 生产批次
	 */
	@ApiModelProperty(value = "生产批次")
	@TableField("PROCESS_LOT")
	private String processLot;
	/**
	 * SFC数量
	 */
	@ApiModelProperty(value = "SFC数量")
	@TableField("SFC_QTY")
	private BigDecimal sfcQty;
	/**
	 * SFC投入数量
	 */
	@ApiModelProperty(value = "SFC投入数量")
	@TableField("INPUT_QTY")
	private BigDecimal inputQty;
	/**
	 * SFC良品数量
	 */
	@ApiModelProperty(value = "SFC良品数量")
	@TableField("DONE_QTY")
	private BigDecimal doneQty;
	/**
	 * 报废数量
	 */
	@ApiModelProperty(value = "报废数量")
	@TableField("SCRAP_QTY")
	private BigDecimal scrapQty;
	@ApiModelProperty(value = "物料版本")
	@TableField(exist = false)
	private String itemVersion;

	/**
	 * 物料
	 */
	@ApiModelProperty(value = "物料")
	@TableField(exist = false)
	private String item;

	/**
	 * 排程
	 */
	@ApiModelProperty(value = "排程")
	@TableField(exist = false)
	private String schedule;

	/**
	 * 当前设备
	 */
	@ApiModelProperty(value = "当前设备")
	@TableField(exist = false)
	private String device;

	/**
	 * 完成率
	 */
	@ApiModelProperty(value = "完成率")
	@TableField(exist = false)
	private BigDecimal completion;
}
