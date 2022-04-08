package com.itl.mes.core.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "OperationVo",description = "保存工序数据")
public class OperationVo implements Serializable {

    @ApiModelProperty(value="OP:SITE,WORK_SHOP,PRODUCT_LINE,OPERATION,VERSION【PK】")
    private String bo;

    @ApiModelProperty(value="工厂【UK】")
    private String site;

    @ApiModelProperty(value="工序编号【UK】")
    private String operation;

    @ApiModelProperty(value="版本号【UK】")
    private String version;

    @ApiModelProperty(value="工序名称")
    private String operationName;

    @ApiModelProperty(value="所属产线 【UK】")
    private String productionLine;

    @ApiModelProperty(value="描述")
    private String operationDesc;

    @ApiModelProperty(value="状态")
    private String state;

    @ApiModelProperty(value="是否当前版本 Y是 N否")
    private String isCurrentVersion;

    @ApiModelProperty(value="工序类型 N：普通S：特殊T：测试")
    private String operationType;

    @ApiModelProperty(value="最大经过次数")
    private Integer maxTimes;

    @ApiModelProperty(value="重测次数")
    private Integer repeatTestTimes;

    @ApiModelProperty(value="缺省工位")
    private String defaultStation;

    @ApiModelProperty(value="工位类型")
    private String stationType;

    @ApiModelProperty(value="缺省不良代码")
    private String defaultNcCode;

    @ApiModelProperty(value="不良代码组")
    private String ncGroup;

    @ApiModelProperty(value="修改日期")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "GMT+8"
    )
    private Date modifyDate;

    @ApiModelProperty( value = "自定义数据属性和值" )
    private List<CustomDataAndValVo> customDataAndValVoList;

    @ApiModelProperty( value = "自定义数据属性和值，保存时传入" )
    private List<CustomDataValVo> customDataValVoList;

    @ApiModelProperty(value = "车间")
    private String workShop;

    @ApiModelProperty(value = "车间名称")
    private String workShopName;
}
