package com.itl.mes.core.api.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.mes.core.api.entity.Sfc;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "QualityCheckListVO",description = "保存与添加检验单基础数据")
public class QualityCheckListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="id")
    private int id;

    @ApiModelProperty(value="检验编号")
    private String checkCode;

    @ApiModelProperty(value="检验工序BO")
    private String operationBo;

    @ApiModelProperty(value="检验产品BO")
    private String itemBo;

    @ApiModelProperty(value="检验人")
    private String checkUser;

    @ApiModelProperty(value="检验方式（0：自检，1：品检）")
    private String checkWay;

    @ApiModelProperty(value="检验类型（0:首件检验，1:巡检检验，2:最终检验）")
    private String checkType;

    @ApiModelProperty(value="检验状态（0：待检验,1:待审核，2：已完成）")
    private String checkState;

    @ApiModelProperty(value="检验控制计划BO")
    private String parameterBO;

    @ApiModelProperty(value="检验控制计划")
    private String parameterName;

    @ApiModelProperty(value="创建人")
    private String createUser;

    @ApiModelProperty(value="创建时间")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date createDate;

    @ApiModelProperty(value="检验工序名")
    private String operationName;

    @ApiModelProperty(value="检验产品名")
    private String itemName;

    @ApiModelProperty(value="创建人名")
    private String createName;

    @ApiModelProperty(value="检验人名")
    private String checkUserName;

    @ApiModelProperty(value="实测值")
    private String checkValue;

    @ApiModelProperty(value="检验结果")
    private String checkResult;

    @ApiModelProperty(value="检验描述")
    private String checkRemark;

    @ApiModelProperty(value="图片地址")
    private String filePath;

    @ApiModelProperty(value="批次ID")
    private String sfc;

    @ApiModelProperty(value="批次")
    private Sfc sfcEntity;

    @ApiModelProperty(value="设备名称")
    private String checkDeviceName;

    @ApiModelProperty(value="设备编号")
    private String checkDevice;

    @ApiModelProperty(value="车间BO")
    private String workshopBo;

    @ApiModelProperty(value="车间名称")
    private String workshopName;

    @ApiModelProperty(value="物料料号")
    private String item;

    @ApiModelProperty(value="物料描述")
    private String itemDesc;

    @ApiModelProperty(value = "检验工序集合")
    private List<OperationParamsVo> operationList;

    @ApiModelProperty(value="工单编码")
    private String shopOrder;

    @ApiModelProperty("设备表id")
    private String myDeviceId;

    @ApiModelProperty("不合格率")
    private BigDecimal unqualified;
}
