package com.itl.mes.me.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "RepairSnInfoVov", description = "SN生命周期 维修记录")
public class RepairSnInfoVo implements Serializable {

    @ApiModelProperty(value = "维修工位")
    private String repairStation;

    @ApiModelProperty(value = "故障工位")
    private String badStation;

    @ApiModelProperty(value = "维修方式")
    private String repairMethod;

    @ApiModelProperty(value = "维修原因(字典维护)")
    private String repairReason;

    @ApiModelProperty(value = "维修备注")
    private String remark;

    @ApiModelProperty(value = "责任单位")
    private String dutyUnit;

    @ApiModelProperty(value = "替换物料的item")
    private String replaceItemBo;

    @ApiModelProperty(value = "替换组件的item")
    private String replaceSfcItemBo;

    @ApiModelProperty(value = "维修时间")
    private Date createDate;
}
