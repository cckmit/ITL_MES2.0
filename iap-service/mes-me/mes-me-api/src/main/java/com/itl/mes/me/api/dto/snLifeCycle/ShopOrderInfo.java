package com.itl.mes.me.api.dto.snLifeCycle;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author yaoxiang
 * @date 2021/1/28
 * @since JDK1.8
 */
@Data
@ApiModel("SN-ShopOrder")
public class ShopOrderInfo {
    @ApiModelProperty("工单号")
    private String shopOrder;
    @ApiModelProperty("工单数量")
    private BigDecimal orderQty;
    @ApiModelProperty("产品编码")
    private String item;
    @ApiModelProperty("排程号")
    private String scheduleNo;
    @ApiModelProperty("线别")
    private String productLine;
    @ApiModelProperty("排程数量")
    private BigDecimal scheduleQty;
    @ApiModelProperty("计划开始时间")
    private Date startDate;
    @ApiModelProperty("计划完成时间")
    private Date endDate;
    @ApiModelProperty("排程状态")
    private Integer scheduleState;
}
