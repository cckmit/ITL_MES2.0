package com.itl.mes.core.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value="ItemFullVo",description="物料维护")
public class ItemFullVo implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value="物料编码")
    @NotBlank
    private String item;

    @ApiModelProperty(value="版本")
    @NotBlank
    private String version;

    @ApiModelProperty(value="是否为当前版本,Y:是当前版本、N:非当前版本")
    @NotBlank
    private String isCurrentVersion;

    @ApiModelProperty(value="物料名称")
    private String itemName;

    @ApiModelProperty(value="物料描述")
    private String itemDesc;

    @ApiModelProperty(value="物料单位")
    private String itemUnit;

    @ApiModelProperty(value="物料状态")
    @NotBlank
    private String itemState;

    @ApiModelProperty(value="物料类型")
    @NotBlank
    private String itemType;

    @ApiModelProperty(value="线边仓")
    private String wareHouse;

    @ApiModelProperty(value="工艺路线")
    private String router;

    @ApiModelProperty(value="工艺路线版本")
    private String routerVersion;

    @ApiModelProperty(value="物料清单")
    private String bom;

    @ApiModelProperty(value="物料清单版本")
    private String bomVersion;

    @ApiModelProperty(value="批量数")
    @NotNull
    private BigDecimal lotSize;

    @ApiModelProperty(value="修改日期")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date modifyDate;

    @ApiModelProperty("图号")
    private String drawingNo;

    @ApiModelProperty( value="可用物料组" )
    private List<String> availableItemGroupList;

    @ApiModelProperty( value="已分配物料组" )
    private List<String> assignedItemGroupList;

    @ApiModelProperty( value = "自定义数据属性和值" )
    private List<CustomDataAndValVo> customDataAndValVoList;

    @ApiModelProperty( value = "自定义数据属性和值，保存时传入" )
    private List<CustomDataValVo> customDataValVoList;

}
