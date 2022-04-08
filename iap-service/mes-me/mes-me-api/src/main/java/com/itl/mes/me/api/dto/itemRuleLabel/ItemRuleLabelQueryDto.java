package com.itl.mes.me.api.dto.itemRuleLabel;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * @author yaoxiang
 * @date 2021/1/21
 * @since JDK1.8
 */
@Data
public class ItemRuleLabelQueryDto {
    private Page page;
    private String item;
    private String shopOrder;
    private String itemName;
    private int orderQTY;
    private String site;
}
