package com.itl.mes.me.api.dto.itemRuleLabel;

import com.itl.mes.me.api.entity.ItemRuleLabelDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yaoxiang
 * @date 2021/1/21
 * @since JDK1.8
 */
@Data
public class ItemRuleLabelSaveDto {
    @ApiModelProperty("bo")
    private String bo;
    @ApiModelProperty("物料")
    private String itemBo;
    @ApiModelProperty("编码规则")
    private String codeRuleType;
    @ApiModelProperty("标签")
    private String labelBo;
    @ApiModelProperty("标签类型")
    private String labelType;
    @ApiModelProperty("工厂")
    private String site;
    @ApiModelProperty("变量字段对应关系")
    private List<ItemRuleLabelDetail> details;
}
