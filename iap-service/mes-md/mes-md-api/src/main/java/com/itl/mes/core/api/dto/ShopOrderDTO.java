package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "ShopOrderDTO",description = "工单DTO")
public class ShopOrderDTO {
    @ApiModelProperty(value = "分页信息")
    private Page page;

    @ApiModelProperty(value = "车间")
    private String workShop;

    @ApiModelProperty(value = "工单编码")
    private String shopOrder;

    @ApiModelProperty(value = "物料编码")
    private String item;

    @ApiModelProperty(value = "物料名称")
    private String itemName;

    @ApiModelProperty(value = "物料描述")
    private String itemDesc;

    @ApiModelProperty(value="计划开始日期")
//    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private String planStartDate;

    @ApiModelProperty(value="计划结束日期")
//    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private String planEndDate;

    private String site;

    @ApiModelProperty(value="单条工单bo")
    private String shopOrderBo;

    @ApiModelProperty(value="工单bo集合")
    private List<String> bos;

    @ApiModelProperty(value="是否加急")
    private String isUrgent;

    @ApiModelProperty(value="工单下达数量")
    private BigDecimal operationOrderQty;

    //210813 add
    @ApiModelProperty(value = "工单状态")
    private String shopOrderState;

    @ApiModelProperty(value = "关键字")
    private String keyWord;

    //关键字拆分后的集合
    List<String> itemDescKeyWordList;
}
