package com.itl.iap.mes.api.dto.toolgroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @auth liuchenghao
 * @date 2021/1/15
 */
@Data
@ApiModel(value = "ToolGroupAttachResDTO",description = "工具附加对象实体")
public class ToolGroupAttachResDTO {

    @ApiModelProperty(value = "BO")
    private String bo;

    @ApiModelProperty(value = "附加对象类型")
    private Integer type;

    @ApiModelProperty(value = "附加对象名称")
    private String contextName;

    @ApiModelProperty(value = "附加对象BO")
    private String contextBo;

    @ApiModelProperty(value = "附加对象数量")
    private String qty;

    @ApiModelProperty(value = "详细信息")
    private String contextDesc;

}
