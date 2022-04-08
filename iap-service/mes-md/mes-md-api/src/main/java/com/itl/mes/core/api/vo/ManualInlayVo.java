package com.itl.mes.core.api.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ManualInlayVo {

    @ApiModelProperty(value="用户名称")
    @Excel( name="用户名称", orderNum="3" )
    private String userName;

    @ApiModelProperty(value="内部工号")
    @Excel( name="内部工号", orderNum="8" )
    private String  insideNo;

//    @ApiModelProperty("派工系数")
//    @Excel( name="派工系数", orderNum="8" )
//    private String dispatchRatio;

    @ApiModelProperty("物料描述")
    @Excel( name="物料描述", orderNum="8" )
    private String itemDesc;

    @ApiModelProperty(value="派工数量")
    @Excel( name="派工数量", orderNum="8", type=10)
    private BigDecimal dispatchQty;
}
