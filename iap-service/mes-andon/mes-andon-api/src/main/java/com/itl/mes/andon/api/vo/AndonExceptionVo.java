package com.itl.mes.andon.api.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@ApiModel(value = "AndonExceptionVO", description = "保存安灯异常数据")
public class AndonExceptionVo {

    @ApiModelProperty(value="id")
    private String id;

    @ApiModelProperty(value = "安灯异常问题编号")
    private String exceptionCode;

    @ApiModelProperty(value = "车间BO")
    private String workshopBo;

    @ApiModelProperty(value = "车间名称")
    private String workshopName;

    @ApiModelProperty(value = "设备")
    private String deviceBo;

    @ApiModelProperty(value = "设备")
    private String deviceName;

    @ApiModelProperty(value = "设备编码")
    private String device;

    @ApiModelProperty(value = "andon类型Bo")
    private String andonTypeBo;

    @ApiModelProperty(value = "andon类型Name")
    private String andonTypeName;

    @ApiModelProperty(value = "andon明细")
    private String andonDetail;

    @ApiModelProperty(value = "不良代码/故障代码")
    private String faultCode;

    @ApiModelProperty(value = "触发人")
    private String triggerUser;

    @ApiModelProperty(value = "触发时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date triggerTime;

    @ApiModelProperty(value = "签到人")
    private String checkUser;

    @ApiModelProperty(value = "签到人名")
    private String checkUserName;

    @ApiModelProperty(value = "签到时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date checkTime;

    @ApiModelProperty(value = "响应时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date responseTime;


    @ApiModelProperty(value = "解除人")
    private String relieveUser;

    @ApiModelProperty(value = "解除时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date relieveTime;

    @ApiModelProperty(value = "持续时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date continueTime;

    @ApiModelProperty(value = "问题描述")
    private String exceptionDesc;

    @ApiModelProperty(value = "处理方法")
    private String processMethod;

    @ApiModelProperty(value = "异常状态")
    private String state;

    @ApiModelProperty(value = "图片")
    private String fileImage;

    @ApiModelProperty(value = "andon明细BO")
    private String andonDescBo;

    @ApiModelProperty(value = "类型")
    private int type;

    @ApiModelProperty("客户端ip地址")
    private String ip;

    @ApiModelProperty("是否验证")
    private String isCheck;
}
