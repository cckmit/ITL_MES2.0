package com.itl.iap.mes.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("m_device_check")
@ApiModel(value="DeviceCheck",description="设备点检表")
public class DeviceCheck extends Model<DeviceCheck> {


    private static final long serialVersionUID = 5201029465285562678L;

    @ApiModelProperty(value="id")
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value="点检计划表id")
    @Length( max = 100 )
    @TableField("check_plan_id")
    private String checkPlanId;

    @ApiModelProperty(value="点检计划名")
    @TableField("checkPlanName")
    private String checkPlanName;

    @ApiModelProperty(value="设备名称")
    @Length( max = 100 )
    @TableField("device_name")
    private String deviceName;

    @ApiModelProperty(value="数据收集组")
    @TableField("dataCollectionName")
    private String dataCollectionName;

    @ApiModelProperty(value="数据收集组id")
    @TableField("dataCollectionId")
    private String dataCollectionId;


    @ApiModelProperty(value="设备点检人账号")
    @TableField("checkUserName")
    private String checkUserName;

    @ApiModelProperty(value="设备点检人名字")
    @TableField("checkRealName")
    private String checkRealName;

    @ApiModelProperty(value="点检时间")
    @TableField("checkTime")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date checkTime;

    @ApiModelProperty(value="点检状态")
    @TableField("state")
    private Integer state;

    @ApiModelProperty(value="点检单编号")
    @Length( max = 100 )
    @TableField("device_check_code")
    private String deviceCheckCode;

    @ApiModelProperty(value="设备类型")
    @TableField("deviceType")
    private String deviceType;

    @ApiModelProperty(value="设备编码")
    @TableField("device_code")
    private String deviceCode;

    @ApiModelProperty(value="巡检人员名字")
    @TableField("inspection_userName")
    private String inspectionUserName;

    @ApiModelProperty(value="设备类型名称")
    @TableField(exist = false)
    private String deviceTypeName;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
