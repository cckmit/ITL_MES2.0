package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(value="CodeRuleItemVo",description="编码规则保存明细")
public class CodeRuleItemVo implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="序号")
    @NotNull
    private Integer seq;

    @ApiModelProperty(value="数据段类型（1固定，2日期，3计数）")
    @NotBlank
    private String sectType;

    @ApiModelProperty(value="段参数")
    private String sectParam;

    @ApiModelProperty(value="当前序列")
    private String currentSequence;

    @ApiModelProperty(value="进制")
    private Integer base;

    @ApiModelProperty(value="序列长度")
    private BigDecimal lenSequence;

    @ApiModelProperty(value="最小序列")
    private String minSequence;

    @ApiModelProperty(value="最大序列")
    private String maxSequence;

    @ApiModelProperty(value="增量")
    private String incr;

    @ApiModelProperty(value="重置序号")
    private String reset;

    @ApiModelProperty(value="修改日期")
    private Date modifyDate;
}