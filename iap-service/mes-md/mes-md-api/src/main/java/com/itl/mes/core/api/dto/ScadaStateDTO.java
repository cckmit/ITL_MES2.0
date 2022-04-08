package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * zrm
 * SCADA状态看板
 */
@Data
@ApiModel(value = "TScadaDataState",description = "TScadaDataState查询实体")
public class ScadaStateDTO {

    @ApiModelProperty(value="主键")
    private String FGUID;

    @ApiModelProperty(value = "设备编号")
    private String  FMachNo;

    @ApiModelProperty(value="机床状态")
    private String FMachState;

    @ApiModelProperty(value="持续时间")
    private String ContinuedTime;

    @ApiModelProperty(value="创建时间")
    private Date CreateData;

    @ApiModelProperty(value="TScadaData主键")
    private String ScadaData_FGUID;

}
