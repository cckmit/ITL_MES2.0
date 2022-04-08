package com.itl.mes.me.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "SendRepairObjVo", description = "维修工位 送修详情数据展示 与 WipLog的BO")
public class SendRepairObjVo implements Serializable {

    @ApiModelProperty(value = "送修详情")
    private List<RepairLogListVo> repairLogListVo;

    @ApiModelProperty(value = "WipLog的BO")
    private String wipLogBo;
}
