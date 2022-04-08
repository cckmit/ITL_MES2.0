package com.itl.mes.me.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("报废或维修完成")
public class ScrapOrRepairFinDto implements Serializable {

    @ApiModelProperty(value = "sfc表的sfc字段")
    private String sfc;

    /**
     * Forced Scrap强制报废 FS
     * Repair Finish维修完成 RF
     */
    @ApiModelProperty(value = "报废指令:FS; 维修完成指令:RF;")
    private String Instructions;

    /**
     * 更新WipLog表的条件
     */
    @ApiModelProperty(value = "WipLog的BO")
    private String wipLogBo;
}
