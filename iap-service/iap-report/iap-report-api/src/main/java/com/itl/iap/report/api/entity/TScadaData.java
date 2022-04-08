package com.itl.iap.report.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@TableName("TScadaData")
public class TScadaData {
    @ApiModelProperty("设备编码")
    @TableField("FMachNo")
    private String fMachNo;

    @ApiModelProperty("设备状态")
    @TableField("FMachState")
    private String fMachState;

    @ApiModelProperty("创建时间")
    @TableField("FMachCreateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date fMachCreateTime;
}
