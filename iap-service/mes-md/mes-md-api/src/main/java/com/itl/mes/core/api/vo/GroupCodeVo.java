package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "GroupCodeVo",description ="不合格代码组中保存不合格代码用")
public class GroupCodeVo implements Serializable {
    @ApiModelProperty(value="不良代码组【UK】")
    private String ncGroup;

    @ApiModelProperty(value="名称")
    private String ncGroupName;

    @ApiModelProperty(value="描述")
    private String ncGroupDesc;
}
