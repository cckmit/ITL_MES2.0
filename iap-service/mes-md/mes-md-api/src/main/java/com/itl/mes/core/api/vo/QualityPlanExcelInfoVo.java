package com.itl.mes.core.api.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QualityPlanExcelInfoVo {
    @ApiModelProperty("物料编码")
    @Excel( name="物料编码", orderNum="0" )
    private String item;

    @ApiModelProperty("工序编码")
    @Excel( name="工序编码", orderNum="1" )
    private String operation;

    @ApiModelProperty("工序名称")
    @Excel( name="工序名称", orderNum="2" )
    private String operationName;

    @ApiModelProperty("检验项")
    @Excel( name="检验项", orderNum="3" )
    private String parameterDesc;

    @ApiModelProperty("检验工具")
    @Excel( name="检验工具", orderNum="4" )
    private String inspectTool;

    @ApiModelProperty("检验方法")
    @Excel( name="检验方法", orderNum="5" )
    private String inspectMethod;

    @ApiModelProperty("上限")
    @Excel( name="上限", orderNum="6" )
    private String upperLimit;

    @ApiModelProperty("目标值")
    @Excel( name="目标值", orderNum="7" )
    private String aimVal;

    @ApiModelProperty("下限")
    @Excel( name="下限", orderNum="8" )
    private String lowerLimit;
}
