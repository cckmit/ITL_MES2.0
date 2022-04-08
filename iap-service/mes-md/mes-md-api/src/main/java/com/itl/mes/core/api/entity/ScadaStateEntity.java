package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * zrm
 * SCADA状态看板
 */
@Data
@TableName("TScadaDataState")
@ApiModel(value="ScadaState",description="Scada状态表")
public class ScadaStateEntity {
    private static final long serialVersionUID = -8384486979728802613L;

    @ApiModelProperty(value="主键")
    @Length( max = 32 )
    @TableField("FGUID")
    private String FGUID;

    @ApiModelProperty(value="设备编号")
    @Length( max = 10 )
    @TableField("FMachNo")
    private String  FMachNo;

    @ApiModelProperty(value="机床状态")
    @Length( max = 1 )
    @TableField("FMachState")
    private String FMachState;

    @ApiModelProperty(value="持续时间")
    @Length( max = 20 )
    @TableField("FGUID")
    private String ContinuedTime;

    @ApiModelProperty(value="创建时间")
    @Length( max = 7 )
    @TableField("CreateDate")
    private Date CreateData;

    @ApiModelProperty(value="TScadaData主键")
    @Length( max = 32 )
    @TableField("ScadaData_FGUID")
    private String ScadaData_FGUID;

}
