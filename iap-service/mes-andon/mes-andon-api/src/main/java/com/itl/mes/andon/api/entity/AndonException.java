package com.itl.mes.andon.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@TableName("andon_exception")
@ApiModel(value = "andon_exception", description = "安灯异常")
@Accessors(chain = true)
public class AndonException {

    private static final long serialVersionUID = -30729856515700265L;

    @Id
    @ApiModelProperty(value="id")
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    @TableField("exception_code")
    @Length(max = 100)
    @ApiModelProperty(value = "安灯异常问题编号")
    private String exceptionCode;

    @TableField("workshop_bo")
    @Length(max = 100)
    @ApiModelProperty(value = "车间BO")
    private String workshopBo;

    @TableField("workshop_name")
    @Length(max = 100)
    @ApiModelProperty(value = "工厂名称")
    private String workshopName;

    @TableField("device_bo")
    @Length(max = 100)
    @ApiModelProperty(value = "设备BO")
    private String deviceBo;

    @TableField("device_name")
    @Length(max = 100)
    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @TableField("andon_type_bo")
    @Length(max = 100)
    @ApiModelProperty(value = "andon类型BO")
    private String andonTypeBo;

    @TableField("andon_type_name")
    @Length(max = 100)
    @ApiModelProperty(value = "andon类型名称")
    private String andonTypeName;

    @TableField("andon_detail")
    @Length(max = 100)
    @ApiModelProperty(value = "andon明细")
    private String andonDetail;

    @TableField("fault_code")
    @Length(max = 100)
    @ApiModelProperty(value = "不良代码/故障代码")
    private String faultCode;

    @TableField("trigger_user")
    @Length(max = 32)
    @ApiModelProperty(value = "触发人")
    private String triggerUser;

    @TableField("trigger_user_name")
    @Length(max = 32)
    @ApiModelProperty(value = "触发人名")
    private String triggerUserName;

    @TableField("trigger_time")
    @ApiModelProperty(value = "触发时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date triggerTime;

    @TableField("check_user")
    @Length(max = 32)
    @ApiModelProperty(value = "签到人")
    private String checkUser;

    @TableField("check_user_name")
    @Length(max = 32)
    @ApiModelProperty(value = "签到人名")
    private String checkUserName;

    @TableField("check_time")
    @ApiModelProperty(value = "签到时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date checkTime;

    @TableField("response_time")
    @ApiModelProperty(value = "响应时间")
    private Long responseTime;


    @TableField("relieve_user")
    @Length(max = 32)
    @ApiModelProperty(value = "解除人")
    private String relieveUser;

    @TableField("relieve_user_name")
    @Length(max = 32)
    @ApiModelProperty(value = "解除人名")
    private String relieveUserName;

    @TableField("relieve_time")
    @ApiModelProperty(value = "解除时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date relieveTime;

    @TableField("continue_time")
    @ApiModelProperty(value = "持续时间")
    private Long continueTime;

    @TableField("exception_desc")
    @Length(max = 255)
    @ApiModelProperty(value = "问题描述")
    private String exceptionDesc;

    @TableField("process_method")
    @Length(max = 255)
    @ApiModelProperty(value = "处理方法")
    private String processMethod;

    @TableField("state")
    @Length(max = 1)
    @ApiModelProperty(value = "异常状态")
    private String state;

    @TableField("file_image")
    @Length(max = 255)
    @ApiModelProperty(value = "图片")
    private String fileImage;

    @TableField("andon_desc_bo")
    @Length(max = 255)
    @ApiModelProperty(value = "andon明细BO")
    private String andonDescBo;

    @TableField("job_ids")
    @Length(max = 255)
    @ApiModelProperty(value = "andon明细BO")
    private String jobIds;

    @TableField("device")
    @Length(max = 255)
    @ApiModelProperty(value = "设备编码")
    private String device;

    @TableField(exist = false)
    @ApiModelProperty("触发人电话")
    private String triggerUserPhone;

    @TableField(exist = false)
    @ApiModelProperty("是否校验")
    private String isCheck;
}
