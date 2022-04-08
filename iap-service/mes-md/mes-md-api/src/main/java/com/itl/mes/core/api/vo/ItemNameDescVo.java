package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

@Data
@ApiModel(value="ItemNameDescVo",description="物料组接受物料数据使用")
public class ItemNameDescVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="物料编码")
    @NotBlank
    private String item;

    @ApiModelProperty(value="版本")
    @NotBlank
    private String version;

    @ApiModelProperty(value="物料名称")
    private String itemName;

    @ApiModelProperty(value="物料描述")
    private String itemDesc;


}
