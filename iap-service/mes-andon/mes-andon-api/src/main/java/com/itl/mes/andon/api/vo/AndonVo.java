package com.itl.mes.andon.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(value = "AndonVo",description = "安灯返回实体")
public class AndonVo {


    @ApiModelProperty("ANDON:SITE,RESOURCE_TYPE,ANDON【PK】")
    private String bo;

    @ApiModelProperty(value="安灯编号")
    private String andon;

    @ApiModelProperty(value="名称")
    private String andonName;

    @ApiModelProperty(value="描述")
    private String andonDesc;

    @ApiModelProperty(value="安灯灯箱")
    private String andonBoxBo;

    @ApiModelProperty(value="安灯类型")
    private String andonTypeBo;

    @ApiModelProperty(value="安灯推送设置")
    private String andonPushBo;

    @ApiModelProperty(value="灯箱名称")
    private String boxName;


    @ApiModelProperty(value="安灯类型名称")
    private String andonTypeName;


    @ApiModelProperty(value="安灯推送名称")
    private String andonPushName;

    @ApiModelProperty(value="关联对象Bo")
    private String relatedObjectBo;

    @ApiModelProperty(value="关联对象名称")
    private String relatedObjectName;


    @ApiModelProperty(value="状态")
    private String state;

    @ApiModelProperty(value="建档日期")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date createDate;

    @ApiModelProperty(value="建档人")
    private String createUser;

    @ApiModelProperty(value="修改日期")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date modifyDate;

    @ApiModelProperty(value="修改人")
    private String modifyUser;

    @ApiModelProperty(value="资源类型")
    private String resourceType;

    @ApiModelProperty(value="安灯类型标识")
    private String andonTypeTag;


}
