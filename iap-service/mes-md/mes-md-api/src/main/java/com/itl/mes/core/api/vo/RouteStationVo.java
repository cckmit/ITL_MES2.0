package com.itl.mes.core.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.mes.core.api.entity.BaseModelHasCus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 工艺路线工步配置表
 * </p>
 *
 * @author pwy
 * @since 2021-3-12
 */
@Data
@ApiModel(value="RouterStationVo",description="工艺路线工步配置")
public class RouteStationVo extends BaseModelHasCus<RouteStationVo> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="ROUTSTATION")
    private Integer bo;

    @ApiModelProperty(value="工厂【UK】")
    private String site;

    @ApiModelProperty(value="工艺路线")
    private String processRoute;

    @ApiModelProperty(value="工艺路线版本")
    private String routeVer;

    @ApiModelProperty(value="工序编码")
    private String workingProcess;

    @ApiModelProperty(value="工序名称")
    private String workingProcessName;

    @ApiModelProperty(value="工步编码")
    private String workStepCode;

    @ApiModelProperty(value="工步名称")
    private String workStepName;

    @ApiModelProperty(value="状态")
    private String state;

    @ApiModelProperty(value="有效性")
    private String effective;

    @ApiModelProperty(value="修改日期")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "GMT+8"
    )
    private Date updateTime;

    @ApiModelProperty(value="修改人")
    private String updatedBy;


    @Override
    protected Serializable pkVal() {
        return this.bo;
    }
}
