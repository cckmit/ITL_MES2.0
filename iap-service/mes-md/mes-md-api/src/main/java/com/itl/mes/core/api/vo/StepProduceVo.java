package com.itl.mes.core.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel(value = "StepProduceVo",description = "线圈生产执行Vo")
@Data
public class StepProduceVo {

    @ApiModelProperty("工单BO")
    private String shopOrderBo;

    @ApiModelProperty("工单")
    private String shopOrder;

    @ApiModelProperty("物料BO")
    private String itemBo;

    @ApiModelProperty("物料")
    private String item;

    @ApiModelProperty("物料名称")
    private String itemName;

    @ApiModelProperty("物料描述")
    private String itemDesc;

    @ApiModelProperty("工序工单数量（工单下达数量）")
    private BigDecimal orderQty;

    @ApiModelProperty("可进站数量")
    private BigDecimal canInputQty;

    @ApiModelProperty("下达时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date releaseTime;

    @ApiModelProperty("工序工单")
    private String operationOrder;

    @ApiModelProperty("进站数量")
    private BigDecimal inputQty;

    @ApiModelProperty("可出站数量")
    private BigDecimal canOutputQty;

    @ApiModelProperty("进站时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date createDate;

    @ApiModelProperty("操作人")
    private String createUser;

    @ApiModelProperty("派工单编码")
    private String dispatchCode;
}
