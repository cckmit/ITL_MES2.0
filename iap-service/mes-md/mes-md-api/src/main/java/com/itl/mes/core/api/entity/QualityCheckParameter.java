package com.itl.mes.core.api.entity;


import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("m_quality_plan_parameter_result")
@ApiModel(value="parameterResult",description="品质检验单工序测试值")
public class QualityCheckParameter extends Model<QualityPlanParameter> {

    private static final long serialVersionUID = 1L;


    @Excel(name = "表BO", orderNum = "17")
    @ApiModelProperty(value = "QPP:SITE,QUALITY_PLAN,VERSION[PK],PARAMETER_NAME")
    @Length(max = 100)
    @TableId(value = "BO", type = IdType.INPUT)
    private String bo;

    @Excel(name = "工厂", orderNum = "16")
    @ApiModelProperty(value = "工厂")
    @Length(max = 32)
    @TableField("SITE")
    private String site;

    @Excel(name = "质量控制计划BO", orderNum = "0")
    @ApiModelProperty(value = "质量控制计划BO")
    @Length(max = 100)
    @TableField("QUALITY_PLAN_BO")
    private String qualityPlanBo;

    @Excel(name = "检查项目描述", orderNum = "3")
    @ApiModelProperty(value = "检查项目描述")
    @Length(max = 200)
    @TableField("PARAMETER_DESC")
    private String parameterDesc;

    @Excel(name = "目标值", orderNum = "4")
    @ApiModelProperty(value = "目标值")
    @Length(max = 32)
    @TableField("AIM_VAL")
    private String aimVal;

    @Excel(name = "上限", orderNum = "5")
    @ApiModelProperty(value = "上限")
    @TableField("UPPER_LIMIT")
    private String upperLimit;

    @Excel(name = "下限", orderNum = "6")
    @ApiModelProperty(value = "下限")
    @TableField("LOWER_LIMIT")
    private String lowerLimit;

    @Excel(name = "检验方法", orderNum = "7")
    @ApiModelProperty(value = "检验方法")
    @Length(max = 64)
    @TableField("INSPECT_METHOD")
    private String inspectMethod;

    @Excel(name = "检验类型", orderNum = "8")
    @ApiModelProperty(value = "检验类型")
    @Length(max = 64)
    @TableField("INSPECT_TYPE")
    private String inspectType;

    @Excel(name = "参数类型", orderNum = "9")
    @ApiModelProperty(value = "参数类型（0：数值，1：布尔，2：文本）")
    @Length(max = 10)
    @TableField("PARAMETER_TYPE")
    private String parameterType;

    @Excel(name = "检验数量", orderNum = "10", type = 10)
    @ApiModelProperty(value = "检验数量")
    @TableField("INSPECT_QTY")
    private Integer inspectQty;

    @Excel(name = "是否启用", orderNum = "11")
    @ApiModelProperty(value = "是否启用（0：未启用，1：启用）")
    @Length(max = 1)
    @TableField("ENABLED")
    private String enabled;

    @Excel(name = "创建人", orderNum = "12")
    @ApiModelProperty(value = "创建人")
    @Length(max = 32)
    @TableField("CREATE_USER")
    private String createUser;

    @Excel(name = "创建时间", orderNum = "13", exportFormat = "yyyy-MM-dd", importFormat = "yyyy-MM-dd")
    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_DATE")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date createDate;

    @Excel(name = "修改人", orderNum = "14")
    @ApiModelProperty(value = "修改人")
    @Length(max = 32)
    @TableField("MODIFY_USER")
    private String modifyUser;

    @Excel(name = "工序名称BO", orderNum = "16")
    @ApiModelProperty(value = "工序名称")
    @Length(max = 32)
    @TableField("OPERATION_BO")
    private String operationBO;

    @Excel(name = "物料名称BO", orderNum = "17")
    @ApiModelProperty(value = "物料名称")
    @Length(max = 100)
    @TableField("ITEM_BO")
    private String itemBo;

    @Excel(name = "检验工具", orderNum = "18")
    @ApiModelProperty(value = "检验工具")
    @Length(max = 64)
    @TableField("INSPECT_TOOL")
    private String inspectTool;

    @Excel(name = "工序名称BO", orderNum = "19")
    @ApiModelProperty(value = "工序名称")
    @Length(max = 32)
    @TableField("OPERATION_NAME")
    private String operationName;

    @Excel(name = "物料名称BO", orderNum = "20")
    @ApiModelProperty(value = "物料名称")
    @Length(max = 100)
    @TableField("ITEM_NAME")
    private String itemName;

    @Excel(name = "修改时间", orderNum = "15", exportFormat = "yyyy-MM-dd hh:mm:ss")
    @ApiModelProperty(value = "修改时间")
    @TableField("MODIFY_DATE")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date modifyDate;

    @Excel(name = "质量控制计划", orderNum = "21")
    @ApiModelProperty(value = "质量控制计划")
    @Length(max = 100)
    @TableField("QUALITY_PLAN")
    private String qualityPlan;

    @Excel(name = "自检实测值", orderNum = "22")
    @ApiModelProperty(value = "自检实测值")
    @Length(max = 100)
    @TableField("SELF_REAL_VALUE")
    private String selfRealValue;

    @Excel(name = "自检结果值", orderNum = "23")
    @ApiModelProperty(value = "自检结果值")
    @Length(max = 100)
    @TableField("SELF_RESULT")
    private String selfResult;

    @Excel(name = "品检实测值", orderNum = "24")
    @ApiModelProperty(value = "品检实测值")
    @Length(max = 100)
    @TableField("QUA_REAL_VALUE")
    private String quaRealValue;

    @Excel(name = "品检结果值", orderNum = "25")
    @ApiModelProperty(value = "品检结果值")
    @Length(max = 100)
    @TableField("QUA_RESULT")
    private String quaResult;

    @ApiModelProperty(value="检验单编号")
    @Length( max = 32 )
    @TableField("CHECK_CODE")
    @Excel( name="检验单编号", orderNum="2" )
    private String checkCode;

    @ApiModelProperty(value="sfc批次")
    @Length( max = 32 )
    @TableField("SFC")
    @Excel( name="sfc批次", orderNum="2" )
    private String sfc;

    public static final String BO = "BO";

    public static final String SITE = "SITE";

    public static final String QUALITY_PLAN_BO = "QUALITY_PLAN_BO";

    public static final String SEQ = "SEQ";

    public static final String PARAMETER_NAME = "PARAMETER_NAME";

    public static final String PARAMETER_DESC = "PARAMETER_DESC";

    public static final String AIM_VAL = "AIM_VAL";

    public static final String UPPER_LIMIT = "UPPER_LIMIT";

    public static final String LOWER_LIMIT = "LOWER_LIMIT";

    public static final String INSPECT_METHOD = "INSPECT_METHOD";

    public static final String INSPECT_TYPE = "INSPECT_TYPE";

    public static final String PARAMETER_TYPE = "PARAMETER_TYPE";

    public static final String INSPECT_QTY = "INSPECT_QTY";

    public static final String ENABLED = "ENABLED";

    public static final String CREATE_USER = "CREATE_USER";

    public static final String CREATE_DATE = "CREATE_DATE";

    public static final String MODIFY_USER = "MODIFY_USER";

    public static final String MODIFY_DATE = "MODIFY_DATE";

    @Override
    protected Serializable pkVal() {
        return this.bo;
    }
}
