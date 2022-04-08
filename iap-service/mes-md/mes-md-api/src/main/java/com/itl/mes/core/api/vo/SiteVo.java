package com.itl.mes.core.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "SiteVo", description = "工厂保存使用")
public class SiteVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="工厂")
    private String site;

    @ApiModelProperty(value="工厂描述")
    private String siteDesc;

    @ApiModelProperty(value="类型")
    private String siteType;

    @ApiModelProperty(value="是否启用标记(缺省为1)")
    private String enableFlag;

    @ApiModelProperty(value="修改日期")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "GMT+8"
    )
    private Date modifyDate;

}
