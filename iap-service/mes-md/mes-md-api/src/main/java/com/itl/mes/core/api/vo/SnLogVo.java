package com.itl.mes.core.api.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author sky,
 * @date 2019/9/25
 * @time 20:12
 */
@Data
@ApiModel(value = "SnLogVo", description = "条码日志表")
public class SnLogVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="物料编码")
    @Excel( name="物料编码", orderNum="0" )
    private String item;

    @ApiModelProperty(value="物料描述")
    @Excel( name="物料描述", orderNum="1" )
    private String itemDesc;

    @ApiModelProperty(value="物料大类")
    @Excel( name="物料大类", orderNum="2" )
    private String materialType;

    @ApiModelProperty(value="计划编号(工单号)")
    @Excel( name="计划编号(工单号)", orderNum="3" )
    private String shopOrder;

    @ApiModelProperty(value="计划数量")
    @Excel( name="计划数量", orderNum="4" )
    private BigDecimal orderQty;

    @ApiModelProperty(value="生成数量")
    @Excel( name="生成数量", orderNum="5" )
    private Integer createQuantity;

    @ApiModelProperty(value="开始号段")
    @Excel( name="开始号段", orderNum="6" )
    private String startNumber;

    @ApiModelProperty(value="结束号段")
    @Excel( name="结束号段", orderNum="7" )
    private String endNumber;

    @ApiModelProperty(value="建档人")
    @Excel( name="建档人", orderNum="8" )
    private String createUser;

    @ApiModelProperty(value="建档日期")
    @Excel( name="建档日期", orderNum="9", exportFormat = "yyyy-MM-dd" )
    private Date createDate;
}
