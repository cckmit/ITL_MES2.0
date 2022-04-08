package com.itl.mes.andon.api.vo;

import com.itl.mes.andon.api.entity.GradePush;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "AndonGradePushVo", description = "安灯配置人员")
public class AndonGradePushVo {

    @ApiModelProperty(value = "预警级别")
    private int andonGrade;

    @ApiModelProperty(value = "配置人员列表")
    private List<GradePush> gradePushes;
}
