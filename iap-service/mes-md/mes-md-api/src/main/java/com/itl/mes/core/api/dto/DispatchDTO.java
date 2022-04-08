package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel(value = "DispatchDTO",description = "Dispatch查询实体")
public class DispatchDTO {

    @ApiModelProperty(value = "分页信息")
    private Page page;

    @ApiModelProperty(value = "派工id")
    private String id;

    @ApiModelProperty(value = "工序工单号")
    private String operationOrder;

    @ApiModelProperty(value = "工序bo")
    private String operationBo;

    @ApiModelProperty(value = "工序")
    private String operation;

    @ApiModelProperty(value = "工序工单数量（工单下达数量）")
    private BigDecimal operationOrderQty;

    @ApiModelProperty(value = "设备")
    private String device;

    @ApiModelProperty("设备集合")
    private String[] deviceList;

    @ApiModelProperty(value = "派工数量")
    private BigDecimal dispatchQty;

    @ApiModelProperty(value = "派工人员")
    private String dispatchOp;

    @ApiModelProperty(value = "物料")
    private String item;

    @ApiModelProperty(value = "物料描述")
    private String itemDesc;

    @ApiModelProperty(value = "任务状态 0：全部 1：已生产 2: 待生产")
    private String taskState;

    @ApiModelProperty(value = "派工单编码")
    private String dispatchCode;

    private String workShop;

    @ApiModelProperty(value = "是否展示已完成的派工单 默认为展示 1:代表过滤掉已完成的派工单")
    private String isShowDoneOrder;

    @ApiModelProperty(value = "备注")
    private String remark;

    private String canPrintQty;
}
