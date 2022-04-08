package com.itl.mes.me.api.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * SN日志表
 *
 * @author cuichonghe
 * @date 2020-12-25 12:36:32
 */
@Data
@TableName("z_sn_log")
@ApiModel(value = "z_sn_log", description = "SN日志表")
public class SnLog implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * idWorker
     */
    @TableId
    @ApiModelProperty(value = "idWorker")
    private String bo;
    /**
     * 工厂
     */
    @ApiModelProperty(value = "工厂")
    @Excel(name = "工厂")
    private String site;
    /**
     * 物料编码
     */
    @ApiModelProperty(value = "物料编码")
    @Excel(name = "物料编码")
    private String item;
    /**
     * 物料大类
     */
    @TableField("MATERIAL_TYPE")
    @ApiModelProperty(value = "物料大类")
    @Excel(name = "物料大类")
    private String materialType;
    /**
     * 计划编号(工单号)
     */
    @TableField("SHOP_ORDER")
    @ApiModelProperty(value = "计划编号(工单号)")
    @Excel(name = "计划编号(工单号)")
    private String shopOrder;
    /**
     * 生成数量
     */
    @TableField("CREATE_QUANTITY")
    @ApiModelProperty(value = "生成数量")
    @Excel(name = "生成数量")
    private Integer createQuantity;
    /**
     * 开始号段
     */
    @TableField("START_NUMBER")
    @ApiModelProperty(value = "开始号段")
    @Excel(name = "开始号段")
    private String startNumber;
    /**
     * 结束号段
     */
    @TableField("END_NUMBER")
    @ApiModelProperty(value = "结束号段")
    @Excel(name = "结束号段")
    private String endNumber;
    /**
     * 建档人
     */
    @TableField("CREATE_USER")
    @ApiModelProperty(value = "建档人")
    @Excel(name = "建档人")
    private String createUser;
    /**
     * 建档日期
     */
    @TableField("CREATE_DATE")
    @ApiModelProperty(value = "建档日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    @Excel(name = "建档日期")
    private Date createDate;


    @ApiModelProperty(value = "物料描述")
    @Excel(name = "物料描述")
    @TableField(exist = false)
    private String itemDesc;

    @ApiModelProperty(value = "计划数量")
    @Excel(name = "计划数量")
    @TableField(exist = false)
    @NotNull
    private BigDecimal orderQty;
}
