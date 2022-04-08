package com.itl.mes.andon.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "安灯异常查询对象")
public class AndonExceptionDTO {

    //分页对象
    private Page page;

    @ApiModelProperty(value = "开始时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date endTime;

    @ApiModelProperty(value = "车间名称")
    private String workShopName;

    @ApiModelProperty(value = "andon类型")
    private String andonTypeName;

    @ApiModelProperty(value = "设备编码")
    private String device;

    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @ApiModelProperty(value = "异常状态")
    private String state;

    @ApiModelProperty(value = "触发人员")
    private String triggerUser;

    @ApiModelProperty(value = "触发人员名字")
    private String triggerUserName;

    @ApiModelProperty(value = "deviceBo")
    private String deviceBo;

    @ApiModelProperty()
    private List<Integer> states;
}
