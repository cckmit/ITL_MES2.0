package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 编码规则明细表
 * </p>
 *
 * @author space
 * @since 2019-06-19
 */
@TableName("m_code_rule_item")
@ApiModel(value="CodeRuleItem",description="编码规则明细表")
public class CodeRuleItem extends Model<CodeRuleItem> {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value="主键")
    @NotBlank
    @Length( max = 100 )
    @TableId(value = "BO", type = IdType.INPUT)
    private String bo;

    @ApiModelProperty(value="工厂")
    @NotBlank
    @Length( max = 32 )
    @TableField("SITE")
    private String site;

    @ApiModelProperty(value="编号规则BO")
    @NotBlank
    @Length( max = 100 )
    @TableField("CODE_RULE_BO")
    private String codeRuleBo;

    @ApiModelProperty(value="序号")
    @NotNull
    @TableField("SEQ")
    private Integer seq;

    @ApiModelProperty(value="数据段类型（1固定，2日期，3计数）")
    @Length( max = 2 )
    @NotBlank
    @TableField("SECT_TYPE")
    private String sectType;

    @ApiModelProperty(value="段参数")
    @Length( max = 40 )
    @TableField("SECT_PARAM")
    private String sectParam;

    @ApiModelProperty(value="当前序列")
    @TableField("CURRENT_SEQUENCE")
    private BigDecimal currentSequence;

    @ApiModelProperty(value="进制")
    @TableField("BASE")
    private Integer base;

    @ApiModelProperty(value="序列长度")
    @TableField("LEN_SEQUENCE")
    private BigDecimal lenSequence;

    @ApiModelProperty(value="最小序列")
    @TableField("MIN_SEQUENCE")
    private BigDecimal minSequence;

    @ApiModelProperty(value="最大序列")
    @TableField("MAX_SEQUENCE")
    private BigDecimal maxSequence;

    @ApiModelProperty(value="增量")
    @TableField("INCR")
    private Integer incr;

    @ApiModelProperty(value="重置序号")
    @Length( max = 1 )
    @TableField("RESET")
    private String reset;

    @ApiModelProperty(value="修改日期")
    @TableField("MODIFY_DATE")
    private Date modifyDate;

    public String getBo() {
        return bo;
    }

    public void setBo(String bo) {
        this.bo = bo;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getCodeRuleBo() {
        return codeRuleBo;
    }

    public void setCodeRuleBo(String codeRuleBo) {
        this.codeRuleBo = codeRuleBo;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getSectType() {
        return sectType;
    }

    public void setSectType(String sectType) {
        this.sectType = sectType;
    }

    public String getSectParam() {
        return sectParam;
    }

    public void setSectParam(String sectParam) {
        this.sectParam = sectParam;
    }

    public BigDecimal getCurrentSequence() {
        return currentSequence;
    }

    public void setCurrentSequence(BigDecimal currentSequence) {
        this.currentSequence = currentSequence;
    }

    public Integer getBase() {
        return base;
    }

    public void setBase(Integer base) {
        this.base = base;
    }

    public BigDecimal getLenSequence() {
        return lenSequence;
    }

    public void setLenSequence(BigDecimal lenSequence) {
        this.lenSequence = lenSequence;
    }

    public BigDecimal getMinSequence() {
        return minSequence;
    }

    public void setMinSequence(BigDecimal minSequence) {
        this.minSequence = minSequence;
    }

    public BigDecimal getMaxSequence() {
        return maxSequence;
    }

    public void setMaxSequence(BigDecimal maxSequence) {
        this.maxSequence = maxSequence;
    }

    public Integer getIncr() {
        return incr;
    }

    public void setIncr(Integer incr) {
        this.incr = incr;
    }

    public String getReset() {
        return reset;
    }

    public void setReset(String reset) {
        this.reset = reset;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public static final String BO = "BO";

    public static final String SITE = "SITE";

    public static final String CODE_RULE_BO = "CODE_RULE_BO";

    public static final String SEQ = "SEQ";

    public static final String SECT_TYPE = "SECT_TYPE";

    public static final String SECT_PARAM = "SECT_PARAM";

    public static final String CURRENT_SEQUENCE = "CURRENT_SEQUENCE";

    public static final String BASE = "BASE";

    public static final String LEN_SEQUENCE = "LEN_SEQUENCE";

    public static final String MIN_SEQUENCE = "MIN_SEQUENCE";

    public static final String MAX_SEQUENCE = "MAX_SEQUENCE";

    public static final String INCR = "INCR";

    public static final String RESET = "RESET";

    public static final String MODIFY_DATE = "MODIFY_DATE";

    @Override
    protected Serializable pkVal() {
        return this.bo;
    }

    @Override
    public String toString() {
        return "CodeRuleItem{" +
                ", bo = " + bo +
                ", site = " + site +
                ", codeRuleBo = " + codeRuleBo +
                ", seq = " + seq +
                ", sectType = " + sectType +
                ", sectParam = " + sectParam +
                ", currentSequence = " + currentSequence +
                ", base = " + base +
                ", lenSequence = " + lenSequence +
                ", minSequence = " + minSequence +
                ", maxSequence = " + maxSequence +
                ", incr = " + incr +
                ", reset = " + reset +
                ", modifyDate = " + modifyDate +
                "}";
    }
}