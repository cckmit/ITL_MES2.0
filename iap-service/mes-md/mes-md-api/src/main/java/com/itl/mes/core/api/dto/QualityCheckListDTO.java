package com.itl.mes.core.api.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

@Data
@ApiModel(value = "检验单查询对象")
public class QualityCheckListDTO {

    //分页对象
    private Page page;

    @ApiModelProperty(value="id")
    private int id;

    @ApiModelProperty(value="检验编号")
    private String checkCode;

    @ApiModelProperty(value="检验产品")
    private String itemBo;

    @ApiModelProperty(value="车间")
    private String workshopBo;

    @ApiModelProperty(value="检验类型")
    private String checkType;

    @ApiModelProperty(value="检验产品")
    private String OperationBo;

    @ApiModelProperty(value="检验产品")
    private String sfc;

    @ApiModelProperty(value="检验状态")
    private String checkState;

    @ApiModelProperty(value="开始时间")
    private String startTime;

    @ApiModelProperty(value="结束时间")
    private String endTime;

    @ApiModelProperty(value="工序名称")
    private String operationName;

    @ApiModelProperty(value="品检人员")
    private String qualityCheckUser;

    @ApiModelProperty(value="品检开始时间")
    private String qcStartTime;

    @ApiModelProperty(value="品检结束时间")
    private String qcEndTime;

    @ApiModelProperty(value="检验结果")
    private String checkResult;
}
