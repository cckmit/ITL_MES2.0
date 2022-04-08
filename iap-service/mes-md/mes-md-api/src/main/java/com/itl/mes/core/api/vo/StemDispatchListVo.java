package com.itl.mes.core.api.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class StemDispatchListVo {
    /**
     * uuid，唯一标识
     */
    @ApiModelProperty(value="唯一标识")
    private String id;
    /**
     * 工步编码
     */
    @ApiModelProperty(value="工步编码")
    @Excel( name="工步编码", orderNum="1" )
    private String workStepCode;
    /**
     * 工步名称
     */
    @ApiModelProperty(value="工步名称")
    @Excel( name="工步名称", orderNum="2" )
    private String workStepName;
    /**
     * 设备编码
     */
    @ApiModelProperty(value="用户名称")
    @Excel( name="用户名称", orderNum="3" )
    private String userName;
    /**
     * 设备名称
     */
    @ApiModelProperty(value="用户编码")
    @Excel( name="用户编码", orderNum="4" )
    private String userId;
    /**
     * 工序工单号
     */
    @ApiModelProperty(value="工序工单号")
    @Excel( name="工序工单号", orderNum="4" )
    private String operationOrder;
    /**
     * 物料编码
     */
    @ApiModelProperty(value="物料编码")
    @Excel( name="物料编码", orderNum="5" )
    private String item;
    /**
     * 物料名称
     */
    @ApiModelProperty(value="物料名称")
    @Excel( name="物料名称", orderNum="6" )
    private String itemName;
    /**
     * 物料描述
     */
    @ApiModelProperty(value="物料描述")
    @Excel( name="物料描述", orderNum="7" )
    private String itemDesc;
    /**
     * 派工数量
     */
    @ApiModelProperty(value="派工数量")
    @Excel( name="派工数量", orderNum="8", type=10)
    private BigDecimal dispatchQty;
    /**
     * 创建人
     */
    @ApiModelProperty(value="创建人")
    @Excel( name="创建人", orderNum="9" )
    private String createUser;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    @Excel( name="创建时间", orderNum="10" )
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;

    /**
     * 主线线径
     */
    @Excel( name="主线线径", orderNum="11" )
    private String mainDiameter;
    /**
     * 主线绕组一
     */
    @Excel( name="主线绕组一", orderNum="12" )
    private String mainCircle1;
    /**
     * 主线绕组二
     */
    @Excel( name="主线绕组二", orderNum="13" )
    private String mainCircle2;
    /**
     * 主线绕组三
     */
    @Excel( name="主线绕组三", orderNum="14" )
    private String mainCircle3;
    /**
     * 主线绕组四
     */
    @Excel( name="主线绕组四", orderNum="15" )
    private String mainCircle4;
    /**
     * 主线绕组五
     */
    @Excel( name="主线绕组五", orderNum="16" )
    private String mainCircle5;
    /**
     * 副线线径
     */
    @Excel( name="副线线径", orderNum="17" )
    private String auxiliaryDiameter;
    /**
     * 副线绕组一
     */
    @Excel( name="副线绕组一", orderNum="18" )
    private String auxiliaryCircle1;
    /**
     * 副线绕组二
     */
    @Excel( name="副线绕组一", orderNum="19" )
    private String auxiliaryCircle2;
    /**
     * 副线绕组三
     */
    @Excel( name="副线绕组一", orderNum="20" )
    private String auxiliaryCircle3;
    /**
     * 副线绕组四
     */
    @Excel( name="副线绕组一", orderNum="21" )
    private String auxiliaryCircle4;
    /**
     * 副线绕组五
     */
    @Excel( name="副线绕组一", orderNum="22" )
    private String auxiliaryCircle5;

    /**
     * 派工单编码，自动生成：DIS+YYYY
     */
    @ApiModelProperty(value="派工单编码，自动生成：DIS+YYYY")
    @Excel( name="派工单编码", orderNum="23" )
    private String stepDispatchCode;

    @ApiModelProperty(value="工单")
    private String shopOrder;

    @ApiModelProperty(value="可报工数量")
    private BigDecimal canReportWorkQty;

    @ApiModelProperty(value="物料bo")
    private String itemBo;

    @ApiModelProperty(value="工单bo")
    private String shopOrderBo;

    @ApiModelProperty(value="工步BO")
    private String workStepCodeBo;

    @ApiModelProperty(value="工序BO")
    private String operationBo;

    @ApiModelProperty(value="物料版本")
    private String itemVersion;

    private String roleId;
}
