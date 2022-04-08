package com.itl.mes.core.api.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value="CodeRuleFullVo",description="编码规则保存")
public class CodeRuleFullVo implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="编码类型")
    @NotBlank
    private String codeRuleType;

    @ApiModelProperty(value="描述")
    private String codeRuleDesc;

    @ApiModelProperty(value="生成编码示例")
    private String codeExample;

    @ApiModelProperty(value="规则明细项")
    List<CodeRuleItemVo> codeRuleItemVoList;

    @ApiModelProperty(value="修改日期")
    @TableField("MODIFY_DATE")
    private Date modifyDate;

}
