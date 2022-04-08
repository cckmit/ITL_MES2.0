package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "NcDispRouterVo",description = "不合格代码保存处置工艺路线用")
public class NcDispRouterVo implements Serializable {

    @ApiModelProperty(value="工艺路线编号")
    private String router;

    @ApiModelProperty(value="工艺路线名称")
    private String routerName;

    @ApiModelProperty(value="版本")
    private String version;
}
