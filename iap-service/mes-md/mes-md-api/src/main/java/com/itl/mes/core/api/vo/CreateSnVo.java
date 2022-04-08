package com.itl.mes.core.api.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sky,
 * @date 2020/2/21
 * @time 10:05
 */
@Data
@ApiModel(value="CreateSnVo",description="条码生成导入使用")
public class CreateSnVo implements Serializable {

    @ApiModelProperty(value="编码规则")
    @Excel( name="编码规则", orderNum="0" )
    private String codeRuleType;

    @ApiModelProperty(value="是否补码")
    @Excel( name="是否补码", orderNum="2" )
    private String whether;

    @ApiModelProperty(value="工单号")
    @Excel( name="工单号", orderNum="3" )
    private String shopOrder;

    @ApiModelProperty(value="条码数量")
    @Excel( name="条码数量", orderNum="3" )
    private String number;
}
