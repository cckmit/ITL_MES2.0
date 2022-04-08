package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName("m_my_device")
@ApiModel(value="MyDevice",description="设备人员表")
@Data
public class MyDevice extends Model<MyDevice> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "ID")
    private String id;

    @ApiModelProperty(value = "用户ID")
    @TableField(value = "USER_ID")
    private String userId;

    @ApiModelProperty(value = "工位BO")
    @TableField(value = "STATION_BO")
    private String stationBo;

    @ApiModelProperty(value = "设备BO")
    @TableField(value = "DEVICE_BO")
    private String deviceBo;

    @ApiModelProperty(value = "0：不启用 1：启用 默认启用")
    @TableField(value = "STATE")
    private String state;

    @ApiModelProperty(value = "设备类型")
    @TableField(value = "DEVICE_TYPE")
    private String deviceType;

    @ApiModelProperty(value = "0：未点检 1：已点检")
    @TableField(value = "SPOT_STATE")
    private String spotState;

    @ApiModelProperty(value = "0：未首检 1：待品检 2：已审核")
    @TableField(value = "FIRST_INS_STATE")
    private String firstInsState;

    @ApiModelProperty(value = "修改时间")
    @TableField(value = "MODIFY_TIME")
    private Date modifyTime;

    @ApiModelProperty(value = "检验单编码")
    @TableField(value = "check_code")
    private String checkCode;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
