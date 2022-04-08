package com.itl.mes.core.api.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value="ShopOrderFullVo",description="工单维护VO")
public class ShopOrderFullVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="工单bo")
    private String bo;

    @ApiModelProperty(value="工单号")
    @NotBlank
    private String shopOrder;

    @ApiModelProperty(value="客户订单")
    private String customerOrder;

    @ApiModelProperty(value="工单描述")
    private String orderDesc;

    @ApiModelProperty(value="工单状态")
    @NotBlank
    private String state;

    @ApiModelProperty(value="是否允许超产(Y/N)")
    private String isOverfulfill;

    @ApiModelProperty(value="超产数量")
    private BigDecimal overfulfillQty;

    @ApiModelProperty(value="排产数量")
    private BigDecimal schedulQty;

    @ApiModelProperty(value="订单数量")
    private BigDecimal orderQty;

    @ApiModelProperty(value="生产数量")
    private BigDecimal releaseQty;

    @ApiModelProperty(value="生产物料")
    @NotBlank
    private String item;

    @ApiModelProperty(value="物料版本")
    @NotBlank
    private String itemVersion;

    @ApiModelProperty(value="物料描述")
    private String itemDesc;

    @ApiModelProperty(value="物料名称")
    private String itemName;

    @ApiModelProperty(value="物料图号")
    private String drawingNo;

    @ApiModelProperty(value="计划产线")
    private String productLine;

    @ApiModelProperty(value="计划产线名称")
    private String productLineDesc;

    @ApiModelProperty(value="计划工艺路线")
    private String router;

    @ApiModelProperty(value="计划工艺路线版本")
    private String routerVersion;

    @ApiModelProperty(value="计划物料清单")
    private String bom;

    @ApiModelProperty(value="计划物料清单版本")
    private String bomVersion;

    @ApiModelProperty(value="计划结束日期")
    @TableField("PLAN_END_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date planEndDate;

    @ApiModelProperty(value="计划开始日期")
    @TableField("PLAN_START_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date planStartDate;

    @ApiModelProperty(value="修改日期")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "GMT+8"
    )
    private Date modifyDate;

    @ApiModelProperty( value = "自定义数据属性和值" )
    private List<CustomDataAndValVo> customDataAndValVoList;

    @ApiModelProperty( value = "自定义数据属性和值，保存时传入" )
    private List<CustomDataValVo> customDataValVoList;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date negotiationTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date fixedTime;

    @ApiModelProperty(value="紧急状态")
    private String emergencyState;

    @ApiModelProperty(value="加急备注")
    private String emergencyBz;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value="订单交期")
    private Date orderDeliveryTime;

    private String processChar;

    private String screwCombination;

    private String colourSys;

    private String site;

    private String createUser;

    private String workShop;

    private String workShopName;

    private BigDecimal completeSetQty;
}
