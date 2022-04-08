package com.itl.mes.core.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "SiteUsrResponseVo",description = "用户工厂关系维护响应")
public class SiteUsrResponseVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="用户")
    private String usr;

    @ApiModelProperty(value="默认工厂")
    private String defaultSites;

    @ApiModelProperty(value="已分配工厂")
    private List<SiteVo> siteVoList;

    @ApiModelProperty(value="修改日期")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "GMT+8"
    )
    private Date modifyDate;
}
