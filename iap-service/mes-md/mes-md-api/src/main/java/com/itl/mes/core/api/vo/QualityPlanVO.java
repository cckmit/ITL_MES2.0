package com.itl.mes.core.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(value = "QualityPlanVO",description = "保存与添加质量控制计划使用")
public class QualityPlanVO {

    @ApiModelProperty(value="质量控制计划")
    private String qualityPlan;

    @ApiModelProperty(value="质量控制计划描述")
    private String qualityPlanDesc;

    @ApiModelProperty(value = "QPP:SITE,QUALITY_PLAN,VERSION[PK],PARAMETER_NAME")
    private String bo;

    @ApiModelProperty(value = "工厂")
    private String site;

    @ApiModelProperty(value = "质量控制计划BO")
    private String qualityPlanBo;

    @ApiModelProperty(value = "检查项目描述")
    private String parameterDesc;

    @ApiModelProperty(value = "目标值")
    private String aimVal;

    @ApiModelProperty(value = "上限")
    private String upperLimit;

    @ApiModelProperty(value = "下限")
    private String lowerLimit;

    @ApiModelProperty(value = "检验方法")
    private String inspectMethod;

    @ApiModelProperty(value = "检验类型")
    private String inspectType;

    @ApiModelProperty(value = "参数类型（0：数值，1：布尔，2：文本）")
    private String parameterType;

    @ApiModelProperty(value = "检验数量")
    private Integer inspectQty;

    @ApiModelProperty(value = "是否启用（0：未启用，1：启用）")
    private String enabled;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date createDate;

    @ApiModelProperty(value = "修改人")
    private String modifyUser;

    @ApiModelProperty(value = "工序名称")
    private String operationBO;

    @ApiModelProperty(value = "物料名称")
    private String itemBo;

    @ApiModelProperty(value = "检验工具")
    private String inspectTool;

    @ApiModelProperty(value = "工序名称")
    private String operationName;

    @ApiModelProperty(value = "物料名称")
    private String itemName;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date modifyDate;

}
