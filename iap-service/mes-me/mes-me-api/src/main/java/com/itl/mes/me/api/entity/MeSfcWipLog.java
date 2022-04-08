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
 * 生产过程日志
 *
 * @author cuichonghe
 * @date 2021-01-25 14:43:26
 */
@Data
@TableName("me_sfc_wip_log")
@ApiModel(value = "me_sfc_wip_log", description = "comments")
@Accessors(chain = true)
public class MeSfcWipLog implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 序号【PK】
	 */
	@TableId(value = "BO",type = IdType.INPUT)
	@ApiModelProperty(value = "序号【PK】")
	private String bo;
	/**
	 * 工厂【UK】
	 */
	@ApiModelProperty(value = "工厂【UK】")
	@TableField("SITE")
	private String site;
	/**
	 * SFC编号【FK】【UK】
	 */
	@ApiModelProperty(value = "SFC编号【FK】【UK】")
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
	 * 工序
	 */
	@ApiModelProperty(value = "工序")
	@TableField("OPERATION_BO")
	private String operationBo;
	/**
	 * 工位
	 */
	@ApiModelProperty(value = "工位")
	@TableField("STATION_BO")
	private String stationBo;
	/**
	 * 设备
	 */
	@ApiModelProperty(value = "设备")
	@TableField("DEVICE_BO")
	private String deviceBo;
	/**
	 * 工号
	 */
	@ApiModelProperty(value = "工号")
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
	 * 物料
	 */
	@ApiModelProperty(value = "物料")
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
	 * 进站时间
	 */
	@ApiModelProperty(value = "进站时间")
	@TableField("IN_TIME")
	private Date inTime;
	/**
	 * 出站时间
	 */
	@ApiModelProperty(value = "出站时间")
	@TableField("OUT_TIME")
	private Date outTime;
	/**
	 * 是否批次过站(false-否,true-是)
	 */
	@ApiModelProperty(value = "是否批次过站(false-否,true-是)")
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
	/**
	 * 是否是重复加工
	 */
	@ApiModelProperty(value = "是否是重复加工")
	@TableField("ISRE_WORK")
	private String isRework;
	/**
	 * 处理结果 OK： 合格过站 NG：不合格过站
	 */
	@ApiModelProperty(value = "处理结果 OK： 合格过站 NG：不合格过站")
	@TableField("RESULT")
	private String result;
	/**
	 * 记录时间
	 */
	@ApiModelProperty(value = "记录时间")
	@TableField("CREATE_DATE")
	private Date createDate;

}
