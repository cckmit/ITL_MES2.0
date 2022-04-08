package com.itl.mes.core.api.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.mes.core.api.entity.Attached;
import com.itl.mes.core.api.entity.QualityPlanParameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author lzh
 * @time 2019/8/29
 */
@Data
@ApiModel(value = "QualityPlanAtParameterVo",description = "保存与添加质量控制计划使用")
public class QualityPlanAtParameterVo implements Serializable {
    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value="质量控制计划")
    private String qualityPlan;

    @ApiModelProperty(value="质量控制计划描述")
    private String qualityPlanDesc;

    @ApiModelProperty(value="是否当前版本(Y：是，N：不是)")
    private String isCurrentVersion;

    @ApiModelProperty(value="修改时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date modifyDate;

    @ApiModelProperty(value="控制明细列表")
    private List <QualityPlanParameter> qpParameterList;

    @ApiModelProperty(value="已附加列表")
    private List<Attached> attachedList;
}
