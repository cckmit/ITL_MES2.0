package com.itl.mes.core.api.dto;

import com.itl.mes.core.api.vo.CustomDataValVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@ApiModel( value = "CustomDataValRequest", description = "自定义数据值保存" )
public class CustomDataValRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @ApiModelProperty( value = "特定对象BO" )
    private String bo;

    @ApiModelProperty( value = "工厂" )
    @NotNull
    private String site;

    @ApiModelProperty( value = "数据类型" )
    @NotNull
    private String customDataType;

    @ApiModelProperty( value = "属性值是否合并，默认false" )
    private Boolean doMerge = false;

    @ApiModelProperty( value = "值数组对象" )
    private List<CustomDataValVo> customDataValVoList;
    
}
