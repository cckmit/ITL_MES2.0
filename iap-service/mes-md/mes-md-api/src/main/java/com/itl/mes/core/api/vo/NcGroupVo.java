package com.itl.mes.core.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "NcGroupVo",description = "保存不良数据组")
public class NcGroupVo implements Serializable {

    @ApiModelProperty(value="NG:SITE,NC_GROUP【PK】")
    private String bo;

    @ApiModelProperty(value="工厂【UK】")
    private String site;

    @ApiModelProperty(value="不合格代码组【UK】")
    private String ncGroup;

    @ApiModelProperty(value="名称")
    private String ncGroupName;

    @ApiModelProperty(value="描述")
    private String ncGroupDesc;

    @ApiModelProperty(value="是否在所有资源上有效")
    private String isAllResource;


    @ApiModelProperty(value="修改日期")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "GMT+8"
    )
    private Date modifyDate;

    @ApiModelProperty( value="可分配工序" )
    private List<NcGroupOperationVo> availableOperationList;

    @ApiModelProperty( value="已分配工序" )
    private List<NcGroupOperationVo> assignedOperationList;

    @ApiModelProperty( value="可分配的不良代码" )
    private List<CodeGroupVo> availabieNcCodeList;

    @ApiModelProperty( value="已分配不良代码" )
    private List<CodeGroupVo> assignedNcCodeList;
}
