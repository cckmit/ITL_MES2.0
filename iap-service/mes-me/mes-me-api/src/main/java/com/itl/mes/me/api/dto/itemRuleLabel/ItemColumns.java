package com.itl.mes.me.api.dto.itemRuleLabel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yaoxiang
 * @date 2021/1/21
 * @since JDK1.8
 */
@Data
@ApiModel("物料字段")
public class ItemColumns {
    @ApiModelProperty("字段")
    private String columnName;
    @ApiModelProperty("字段Label")
    private String columnLabel;
}
