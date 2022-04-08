package com.itl.mes.core.api.vo;

import com.itl.mes.core.api.entity.Device;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel(value = "SfcVo", description = "保存条码信息使用")
public class SfcVo  implements Serializable {

    private static final long serialVersionUID = 3114772494931485523L;
    private String bo;
    @ApiModelProperty(value = "SN【UK】")
    private String sfc;

    @ApiModelProperty(value="SFC数量")
    private BigDecimal sfcQty;

    private BigDecimal ncQty;
    private BigDecimal scrapQty;
    private BigDecimal doneQty;

    @ApiModelProperty(value="物料编码")
    private String item;

    @ApiModelProperty(value="物料【UK】")
    private String itemBo;

    @ApiModelProperty(value="物料名称")
    private String itemName;

    @ApiModelProperty(value="物料描述")
    private String itemDesc;

    @ApiModelProperty(value="工序工单号")
    private String operationOrder;

    @ApiModelProperty(value="工单号")
    private String shopOrder;

    @ApiModelProperty(value="进站数量")
    private BigDecimal inputQty;

    @ApiModelProperty(value = "设备集合")
    private List<Device> devices;

    @ApiModelProperty(value = "设备bo")
    private String deviceBo;

    @ApiModelProperty(value = "sfc状态")
    private String state;

    private String sfcRouterBo;
    private String parentSfcBo;
    private String workShopBo;
}
