package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

@Data
@ApiModel(value = "CodeGroupVo",description = "保存不合格代码组里的不合格代码用")
public class CodeGroupVo implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="不良代码BO")
    @NotBlank
    private String bo;


    @ApiModelProperty(value="不良代码【UK】")
    private String ncCode;

    @ApiModelProperty(value="名称")
    private String ncName;

    @ApiModelProperty(value="描述")
    private String ncDesc;

    @ApiModelProperty(value="状态")
    private String state;

}
