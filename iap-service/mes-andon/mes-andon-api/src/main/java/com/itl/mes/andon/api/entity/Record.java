package com.itl.mes.andon.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 安灯日志
 *
 * @author cuichonghe
 * @date 2020-12-14 14:56:55
 */
@Data
@TableName("andon_record")
@ApiModel(value = "record", description = "安灯日志")
public class Record implements Serializable {
  private static final long serialVersionUID = 1L;

  /** 主键【PK】 */
  @ApiModelProperty(value = "主键【PK】")
  @Length(max = 100)
  @TableId(value = "pid", type = IdType.UUID)
  private String pid;
  /** 工厂 */
  @ApiModelProperty(value = "工厂")
  @TableField("SITE")
  private String site;
  /** 安灯 */
  @ApiModelProperty(value = "安灯")
  @TableField("ANDON_BO")
  private String andonBo;
  /** 资源类型 */
  @ApiModelProperty(value = "资源类型")
  @TableField("RESOURCE_TYPE")
  private String resourceType;
  /** 车间 */
  @ApiModelProperty(value = "车间")
  @TableField("WORK_SHOP_BO")
  private String workShopBo;
  /** 产线 */
  @ApiModelProperty(value = "产线")
  @TableField("PRODUCT_LINE_BO")
  private String productLineBo;
  /** 工位 */
  @ApiModelProperty(value = "工位")
  @TableField("STATION_BO")
  private String stationBo;
  /** 设备 */
  @ApiModelProperty(value = "设备")
  @TableField("DEVICE_BO")
  private String deviceBo;
  /** 异常时间 */
  @ApiModelProperty(value = "异常时间")
  @TableField("ABNORMAL_TIME")
  @JsonFormat(
          pattern = "yyyy-MM-dd HH:mm:ss",
          timezone = "GMT+8"
  )
  private Date abnormalTime;
  /** 设备故障代码(设备异常时使用) */
  @ApiModelProperty(value = "设备故障代码(设备异常时使用)")
  @TableField("FAULT_CODE_BO")
  private String faultCodeBo;
  /** 叫料数量 */
  @ApiModelProperty(value = "叫料数量")
  @TableField("CALL_QUANTITY")
  private BigDecimal callQuantity;
  /** 异常图片 */
  @ApiModelProperty(value = "异常图片")
  @TableField("ABNORMAL_IMG")
  private String abnormalImg;
  /** 异常备注 */
  @ApiModelProperty(value = "异常备注")
  @TableField("ABNORMAL_REMARK")
  private String abnormalRemark;
  /** 修复时间 */
  @ApiModelProperty(value = "修复时间")
  @TableField("REPAIR_TIME")
  @JsonFormat(
          pattern = "yyyy-MM-dd HH:mm:ss",
          timezone = "GMT+8"
  )
  private Date repairTime;
  /** 修复人 */
  @ApiModelProperty(value = "修复人")
  @TableField("REPAIR_MAN")
  private String repairMan;
  /** 修复图片 */
  @ApiModelProperty(value = "修复图片")
  @TableField("REPAIR_IMG")
  private String repairImg;
  /** 修复备注 */
  @ApiModelProperty(value = "修复备注")
  @TableField("REPAIR_REMARK")
  private String repairRemark;
  /** 状态 */
  @ApiModelProperty(value = "状态")
  @TableField("STATE")
  private String state;

  @ApiModelProperty(value = "物料")
  @TableField("ITEM_BO")
  private String itemBo;

  @ApiModelProperty(value = "触发时间")
  @TableField("TRIGGER_TIME")
  @JsonFormat(
          pattern = "yyyy-MM-dd HH:mm:ss",
          timezone = "GMT+8"
  )
  private Date triggerTime;

  @ApiModelProperty(value = "触发人")
  @TableField("TRIGGER_MAN")
  private String triggerMan;

  @ApiModelProperty(value = "接收时间")
  @TableField("RECEIVE_TIME")
  @JsonFormat(
          pattern = "yyyy-MM-dd HH:mm:ss",
          timezone = "GMT+8"
  )
  private Date receiveTime;

  @ApiModelProperty(value = "接收人")
  @TableField("RECEIVE_MAN")
  private String receiveMan;

  @ApiModelProperty(value = "关闭时间")
  @TableField("CLOSE_TIME")
  @JsonFormat(
          pattern = "yyyy-MM-dd HH:mm:ss",
          timezone = "GMT+8"
  )
  private Date closeTime;

  @ApiModelProperty(value = "关闭人")
  @TableField("CLOSE_MAN")
  private String closeMan;

  @ApiModelProperty(value = "计划修复时间")
  @TableField("PLAN_REPAIR_TIME")
  @JsonFormat(
          pattern = "yyyy-MM-dd HH:mm:ss",
          timezone = "GMT+8"
  )
  private Date planRepairTime;

  @ApiModelProperty(value = "异常工时")
  @TableField("ABNORMAL_WORK_HOUR")
  private Double abnormalWorkHour;

  @ApiModelProperty(value = "影响的工单")
  @TableField("AFFECT_SHOP_ORDER")
  private String affectShopOrder;
}
