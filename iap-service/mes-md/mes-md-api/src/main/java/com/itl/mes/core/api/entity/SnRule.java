package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * <p>
 * 条码规则表
 * </p>
 *
 * @author space
 * @since 2019-08-03
 */
@TableName("z_sn_rule")
@ApiModel(value = "SnRule", description = "条码规则表")
public class SnRule extends Model<SnRule> {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "序号")
    @Length(max = 10)
    @TableId(value = "SEQ", type = IdType.INPUT)
    private Integer seq;

    @ApiModelProperty(value = "名称")
    @Length(max = 32)
    @TableField("RULE_NAME")
    private String ruleName;

    @ApiModelProperty(value = "代码")
    @Length(max = 40)
    @TableField("RULE_CODE")
    private String ruleCode;

    @ApiModelProperty(value = "规则种类")
    @Length(max = 20)
    @TableField("RULE_TYPE")
    private String ruleType;


    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public static final String SEQ = "SEQ";

    public static final String RULE_NAME = "RULE_NAME";

    public static final String RULE_CODE = "RULE_CODE";

    public static final String RULE_TYPE = "RULE_TYPE";

    @Override
    protected Serializable pkVal() {
        return this.seq;
    }


    @Override
    public String toString() {
        return "SnRule{" +
                ", seq = " + seq +
                ", ruleName = " + ruleName +
                ", ruleCode = " + ruleCode +
                ", ruleType = " + ruleType +
                "}";
    }
}