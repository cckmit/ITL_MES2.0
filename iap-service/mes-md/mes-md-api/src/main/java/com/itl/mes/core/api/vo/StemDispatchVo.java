package com.itl.mes.core.api.vo;

import com.itl.mes.core.api.entity.OperationOrderAndQty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class StemDispatchVo {
//    @ApiModelProperty(value="设备名称")
//    private String deviceName;
//
//    @ApiModelProperty(value="设备编码")
//    private String device;

    @ApiModelProperty(value="派工系数")
    private BigDecimal dispatchRatio;

    @ApiModelProperty(value="工序工单号")
    private String operationOrder;

    @ApiModelProperty(value="物料编码")
    private String item;

    @ApiModelProperty(value="物料名称")
    private String itemName;

    @ApiModelProperty(value="物料描述")
    private String itemDesc;

    @ApiModelProperty(value="分派数量")
    private BigDecimal assignmentQty;

    @ApiModelProperty(value="实际派工数量")
    private BigDecimal dispatchQty;

    @ApiModelProperty(value="用户名称")
    private String userName;

    @ApiModelProperty(value="用户名称")
    private String userId;

    private BigDecimal totalQty;

    private List<OperationOrderAndQty> operationOrderAndQtyList;
}
