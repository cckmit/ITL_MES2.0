package com.itl.mes.pp.api.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
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
 * 机台产品产能表
 *
 * @author cuichonghe
 * @date 2020-12-17 11:28:39
 */
@Data
@TableName("p_device_item_capacity")
@ApiModel(value = "p_device_item_capacity", description = "机台产品产能表")
public class DeviceItemCapacityEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  /**
   * 机台
   */
  @ApiModelProperty(value = "机台")
  @TableField("DEVICE_BO")
  private String deviceBo;
  /** PK */
  @TableId(type= IdType.AUTO)
  @ApiModelProperty(value = "主键")
  @Excel(name = "主键")
  private String bo;
  /** 车间 */
  @ApiModelProperty(value = "车间")
  @Excel(name = "车间")
  @TableField("WORK_SHOP_BO")
  private String workShopBo;
  /** 工厂 */
  @ApiModelProperty(value = "工厂")
  @Excel(name = "工厂")
  private String site;
  /** 机台 */
  @ApiModelProperty(value = "机台")
  @Excel(name = "机台")
  @TableField("PRODUCT_LINE_BO")
  private String productLineBo;
  /** 物料组 */
  @ApiModelProperty(value = "物料组")
  @Excel(name = "物料组")
  @TableField("ITEM_BO")
  private String itemBo;
  /** 牌号 */
  @ApiModelProperty(value = "牌号")
  @Excel(name = "牌号")
  @TableField(exist = false)
  private String brand;
  /** 色号 */
  @ApiModelProperty(value = "色号")
  @Excel(name = "色号")
  @TableField(exist = false)
  private String color;
  /** 配方组 */
  @ApiModelProperty(value = "配方组")
  @Excel(name = "配方组")
  @TableField(exist = false)
  private String formulaGroup;
  /** bom编号 */
  @ApiModelProperty(value = "bom编号")
  @Excel(name = "bom编号")
  @TableField("BOM_BO")
  private String bom;
  /** bom版本 */
  @ApiModelProperty(value = "bom版本")
  @Excel(name = "bom版本")
  @TableField(exist = false)
  private String version;
  /** 工艺编号 */
  @ApiModelProperty(value = "工艺编号")
  @Excel(name = "工艺编号")
  @TableField(exist = false)
  private String processNumber;
  /** 螺杆组合 */
  @ApiModelProperty(value = "螺杆组合")
  @Excel(name = "螺杆组合")
  @TableField(exist = false)
  private String screwAssembly;
  /** 前处理时间（小时） */
  @ApiModelProperty(value = "前处理时间（小时）")
  @Excel(name = "前处理时间（小时）")
  @TableField("BEFORE_TIME")
  private BigDecimal beforeTime;
  /** 设置时间（小时） */
  @ApiModelProperty(value = "设置时间（小时）")
  @Excel(name = "设置时间（小时）")
  @TableField("SET_TIME")
  private BigDecimal setTime;
  /** 额定产能（公斤/小时） */
  @ApiModelProperty(value = "额定产能（公斤/小时）")
  @Excel(name = "额定产能（公斤/小时）")
  @TableField("RATED_CAPACITY")
  private BigDecimal ratedCapacity;
  /** 后处理时间（小时） */
  @ApiModelProperty(value = "后处理时间（小时）")
  @Excel(name = "后处理时间（小时）")
  @TableField("AFTER_TIME")
  private BigDecimal afterTime;
  /** 安全设置时间（小时） */
  @ApiModelProperty(value = "安全设置时间（小时）")
  @Excel(name = "安全设置时间（小时）")
  @TableField("SECURITY_SET_TIME")
  private BigDecimal securitySetTime;
  /** 创建人 */
  @ApiModelProperty(value = "创建人")
  @Excel(name = "创建人")
  @TableField("CREATED_BY")
  private String createdBy;
  /** 创建时间 */
  @ApiModelProperty(value = "创建时间")
  @Excel(name = "创建时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
  @TableField("CREATED_TIME")
  private Date createdTime;
  /** 更新人 */
  @ApiModelProperty(value = "更新人")
  @Excel(name = "更新人")
  @TableField("UPDATED_BY")
  private String updatedBy;
  /** 更新时间 */
  @ApiModelProperty(value = "更新时间")
  @Excel(name = "更新时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
  @TableField("UPDATED_TIME")
  private Date updatedTime;


  /** 工艺特性 */
  @ApiModelProperty(value = "工艺特性")
  @Excel(name = "工艺特性")
  @TableField(exist = false)
  private String processCharacteristics;

  /** 物料描述 */
  @TableField(exist = false)
  @ApiModelProperty(value = "物料描述")
  @Excel(name = "物料描述")
  private String itemDesc;
  /** 物料版本 */
  @TableField(exist = false)
  @ApiModelProperty(value = "物料版本")
  private String itemVersion;

  /** 物料组 */
  @TableField(exist = false)
  @ApiModelProperty(value = "物料组")
  @Excel(name = "物料组")
  private String groupDesc;

  @TableField(exist = false)
  @ApiModelProperty(value = "设备编码")
  private String device;

  @TableField(exist = false)
  @ApiModelProperty(value = "设备名称")
  private String deviceName;

  @TableField(exist = false)
  @ApiModelProperty(value = "物料名称")
  private String itemName;
}
