package com.itl.mes.core.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ReworkVo implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "SFC不合格记录，关联me_sfc_nc_log ")
    private String ngLogBo;

    @ApiModelProperty(value = "sfc条码")
    private String sfc;

    @ApiModelProperty(value = "工序工单号")
    private String operationOrder;

    @ApiModelProperty(value = "工单号")
    private String shopOrder;

    @ApiModelProperty(value = "物料编码")
    private String item;

    @ApiModelProperty(value = "物料名称")
    private String itemName;

    @ApiModelProperty(value = "物料描述")
    private String itemDesc;

    @ApiModelProperty(value = "工序")
    private String operation;

    @ApiModelProperty(value = "工序名称")
    private String operationName;

    @ApiModelProperty(value = "返修数量")
    private BigDecimal ncQty;

    @ApiModelProperty(value = "返修时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date recordTime;

    @ApiModelProperty(value = "设备编码")
    private String device;

    @ApiModelProperty(value = "设备名称")
    private String deviceName;

    @ApiModelProperty(value = "送修人员")
    private String userName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "不良代码")
    private String ncCode;

    @ApiModelProperty(value = "不良代码bo")
    private String ncCodeBo;

    @ApiModelProperty(value = "不良名称")
    private String ncName;

    @ApiModelProperty(value = "SFC过程")
    private String wipLogBo;

    @ApiModelProperty(value = "流程ID")
    private String opIds;

    @ApiModelProperty(value = "工序BO")
    private String operationBo;

    private String userBo;
}
