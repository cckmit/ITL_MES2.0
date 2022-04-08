package com.itl.mes.me.api.dto.itemRuleLabel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.itl.mes.me.api.entity.ItemRuleLabelDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yaoxiang
 * @date 2021/1/21
 * @since JDK1.8
 */
@Data
@ApiModel("物料规则模板listDto")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ItemRuleLabelShowDto {
    @ApiModelProperty("bo")
    private String bo;

    @ApiModelProperty("itemBo")
    private String itemBo;
    @ApiModelProperty("物料编码")
    private String item;
    @ApiModelProperty("物料名称")
    private String itemName;

    @ApiModelProperty("shopOrderBo")
    private String shopOrderBo;
    @ApiModelProperty("工单号")
    private String shopOrder;
    @ApiModelProperty("订单数量")
    private int orderQTY;


    @ApiModelProperty("codeRuleBo")
    private String codeRuleBo;
    @ApiModelProperty("编码规则类型")
    private String codeRuleType;

    @ApiModelProperty("labelBo")
    private String labelBo;
    @ApiModelProperty("标签模板")
    private String label;
    @ApiModelProperty("标签类型")
    private String labelType;

    @ApiModelProperty("变量字段对应关系")
    private List<ItemRuleLabelDetail> details;
}
