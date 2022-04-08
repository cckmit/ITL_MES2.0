package com.itl.mes.core.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "NcCodeVo", description = "保存不良数据使用")
public class NcCodeVo implements Serializable {

    @ApiModelProperty(value="工厂【UK】")
    private String site;

    @ApiModelProperty(value="不良代码【UK】")
    private String ncCode;

    @ApiModelProperty(value="名称")
    private String ncName;

    @ApiModelProperty(value="描述")
    private String ncDesc;

    @ApiModelProperty(value="状态")
    private String state;

    @ApiModelProperty(value="不良类型[F故障、D缺陷、R修复]")
    private String ncType;

    @ApiModelProperty(value="优先级")
    private Integer priority;

    @ApiModelProperty(value="最大不良限制(SFC)")
    private Integer maxNcLimit;

    @ApiModelProperty(value="严重性[0无、2低、3中、5高，缺省为3中]")
    private String severity;

    @ApiModelProperty(value="修改日期")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "GMT+8"
    )
    private Date modifyDate;

    @ApiModelProperty( value="可分配不合格代码组" )
    private List<GroupCodeVo> availableNcGroupList;

    @ApiModelProperty( value="已分配不合格代码组" )
    private List<GroupCodeVo> assignedNcGroupList;

    @ApiModelProperty( value="可分配工艺路线")
    private List<NcDispRouterVo> availableNcDispRouterVos;

    @ApiModelProperty( value="已分配工艺路线")
    private List<NcDispRouterVo> assignedNcDispRouterVos;

    @ApiModelProperty( value = "自定义数据属性和值" )
    private List<CustomDataAndValVo> customDataAndValVoList;

    @ApiModelProperty( value = "自定义数据属性和值，保存时传入" )
    private List<CustomDataValVo> customDataValVoList;
}
