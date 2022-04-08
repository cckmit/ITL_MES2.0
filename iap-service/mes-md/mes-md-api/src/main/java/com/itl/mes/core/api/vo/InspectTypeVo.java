package com.itl.mes.core.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author lzh
 * @date 2019/8/28
 * @time 14:47
 */
@Data
@ApiModel(value = "InspectTypeVo", description = "查询检验类型使用")
public class InspectTypeVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="检验类型")
    private String inspectType;

    @ApiModelProperty(value="检验类型名称")
    private String inspectTypeName;

    @ApiModelProperty(value="检验类型描述")
    private String inspectTypeDesc;

    @ApiModelProperty(value="状态")
    private String state;

    @ApiModelProperty(value="车间是否必要(0:非，1:必)")
    @Length( max = 10 )
    private String requiredWorkShop;

    @ApiModelProperty(value="产线是否必要(0:非，1:必)")
    @Length( max = 10 )
    private String requiredProductLine;

    @ApiModelProperty(value="物料是否必要(0:非，1:必)")
    @Length( max = 10 )
    private String requiredItem;

    @ApiModelProperty(value="工序是否必要(0:非，1:必)")
    @Length( max = 10 )
    private String requiredOperation;


    @ApiModelProperty(value="工位是否必要(0:非，1:必)")
    @Length( max = 10 )
    private String requiredStation;

    @ApiModelProperty(value="工单是否必要(0:非，1:必)")
    @Length( max = 10 )
    private String requiredShopOrder;

    @ApiModelProperty(value="产品条码是否必要(0:非，1:必)")
    @Length( max = 10 )
    private String requiredSfc;

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

}
