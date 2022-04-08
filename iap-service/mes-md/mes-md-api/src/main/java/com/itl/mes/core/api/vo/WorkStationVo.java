package com.itl.mes.core.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "WorkShopVo", description = "车间保存使用")
public class WorkStationVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="OPS:SITE,WORKSTATION")
    private String bo;

    @ApiModelProperty(value="车间")
    private String workShop;

    @ApiModelProperty(value="工厂【UK】")
    private String site;

    @ApiModelProperty(value="工步编码")
    private String workStepCode;

    @ApiModelProperty(value="工步名称")
    private String workStepName;

    @ApiModelProperty(value="工步描述")
    private String workStepDesc;

    @ApiModelProperty(value="工步所属工序")
    private String workingProcess;

    @ApiModelProperty(value="状态")
    private String state;

    @ApiModelProperty(value="修改日期")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date updateTime;

    @ApiModelProperty(value="修改人")
    private String updatedBy;

    @ApiModelProperty(value="版本号【UK】")
    private String version;

    @ApiModelProperty( value = "自定义数据属性和值" )
    private List<CustomDataAndValVo> customDataAndValVoList;

    @ApiModelProperty( value = "自定义数据属性和值，保存时传入" )
    private List<CustomDataValVo> customDataValVoList;
}
