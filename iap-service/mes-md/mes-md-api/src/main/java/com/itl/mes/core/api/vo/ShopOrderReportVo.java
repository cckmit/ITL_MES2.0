package com.itl.mes.core.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value="ShopReportVo",description="工单查询报表")
public class ShopOrderReportVo {

    @ApiModelProperty("工单")
    private String shopOrder;

    @ApiModelProperty("物料")
    private String item;

    @ApiModelProperty("物料名称")
    private String itemName;

    @ApiModelProperty("物料描述")
    private String itemDesc;

    @ApiModelProperty("计划开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date planStartDate;

    @ApiModelProperty("计划结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date planEndDate;

    @ApiModelProperty("待生产数量")
    private BigDecimal waitQty;

    @ApiModelProperty("在制总数量")
    private BigDecimal makingTotalQty;

    @ApiModelProperty("在制详情")
    private List<MakingDetailsVo> makingDetails;

    @ApiModelProperty("已完成数量")
    private BigDecimal doneQty;

    @ApiModelProperty("入库数量")
    private BigDecimal goToStockQty;

    @ApiModelProperty("工单数量")
    private BigDecimal orderQty;

    @ApiModelProperty("工单bo")
    private String shopOrderBo;
    private Integer completeSetQty;
    private String stateBo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endTime;


    @Data
    @ApiModel(value="MakingDetailsVo",description="在制详情信息")
    public static class MakingDetailsVo{

        @ApiModelProperty("工序BO")
        private String operationBo;

        @ApiModelProperty("工序")
        private String operation;

        @ApiModelProperty("工序名称")
        private String operationName;

        @ApiModelProperty("在制数量（单个工序）")
        private BigDecimal makingQty;
    }

}
