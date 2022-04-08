package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "LogicVo",description = "逻辑编号VO")
public class LogicVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="逻辑编号")
    private String logicNo;

    @ApiModelProperty(value="版本")
    private String version;

    @ApiModelProperty(value="描述")
    private String logicDesc;

    @ApiModelProperty(value="是否当前版本Y/N")
    private String isCurrentVersion;

    @ApiModelProperty(value="SQL内容")
    private String content;

    @ApiModelProperty(value="修改日期")
    private Date modifyDate;
}
