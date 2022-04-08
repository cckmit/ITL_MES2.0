package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@ApiModel(value = "StepProduceDTO",description = "线圈生产执行DTO")
@Data
public class StepProduceDTO {

    @ApiModelProperty(value = "分页信息")
    private Page page;

    @ApiModelProperty(value = "工序BO")
    private String operationBo;

    @ApiModelProperty(value = "工单BO")
    private String shopOrderBo;

    @ApiModelProperty(value = "物料编码")
    private String item;

    @ApiModelProperty(value = "物料名称")
    private String itemName;

    @ApiModelProperty(value = "物料描述")
    private String itemDesc;

    @ApiModelProperty(value = "工序工单")
    private String operationOrder;

    @ApiModelProperty(value = "进出站数量")
    private BigDecimal qty;

    @ApiModelProperty(value = "物料BO")
    private String itemBo;

    @ApiModelProperty(value = "报工人")
    private String reportWorkUser;

    @ApiModelProperty(value = "工单编码")
    private String shopOrder;

    @ApiModelProperty(value = "派工单编码")
    private String dispatchCode;

    @ApiModelProperty("物料描述关键字，格式：70*650*热*")
    private String itemDescKeyWord;


    private List<String> itemDescKeyWordList;
}
