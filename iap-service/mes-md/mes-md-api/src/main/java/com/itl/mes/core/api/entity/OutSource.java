package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("m_outsource")
public class OutSource {
    @ApiModelProperty("主键bo")
    @TableId(value = "BO", type = IdType.INPUT)
    private String bo;

    @ApiModelProperty("委外单编号")
    @TableField("outsource_no")
    private String outsourceNo;

    @ApiModelProperty("异常单编号")
    @TableField("exception_code")
    private String exceptionCode;

    @ApiModelProperty("设备编码")
    @TableField("device")
    private String device;

    @ApiModelProperty("设备名称")
    @TableField("device_name")
    private String deviceName;

    @ApiModelProperty("车间bo")
    @TableField("work_shop_bo")
    private String workShopBo;

    @ApiModelProperty("委外申请人")
    @TableField("outsource_user")
    private String outsourceUser;

    @ApiModelProperty("委外原因")
    @TableField("outsource_desc")
    private String outsourceDesc;

    @ApiModelProperty("维修费用")
    @TableField("repair_price")
    private BigDecimal repairPrice;

    @ApiModelProperty("维修人或单位")
    @TableField("repair_user_name")
    private String repairUserName;

    @ApiModelProperty("委外结果（1ok ,0NG,2待确认）")
    @TableField("outsource_result")
    private int outsourceResult;

    @ApiModelProperty("备注")
    @TableField("outsource_remark")
    private String outsourceRemark;

    @ApiModelProperty("创建时间")
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("委外结果不为ok的都是委外中，ok是委外完成")
    @TableField("outsource_state")
    private String outsourceState;

    @ApiModelProperty("车间名称")
    @TableField(exist = false)
    private String workShopName;

    @ApiModelProperty("申请人")
    @TableField(exist = false)
    private String realName;


}
