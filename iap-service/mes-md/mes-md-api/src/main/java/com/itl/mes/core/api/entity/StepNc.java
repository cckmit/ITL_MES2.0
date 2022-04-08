package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("d_dy_step_nc")
@ApiModel(value="StepNc",description="工步不良记录表")
public class StepNc extends Model<StepNc> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="唯一标识")
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value="工单BO")
    @TableField("shop_order_bo")
    private String shopOrderBo;

    @ApiModelProperty(value="物料BO")
    @TableField("item_bo")
    private String itemBo;

    @ApiModelProperty(value="不良bo")
    @TableField("nc_code_bo")
    private String ncCodeBo;

    @ApiModelProperty(value="不良or报废数量")
    @TableField("nc_qty")
    private BigDecimal ncQty;

    @ApiModelProperty(value="不良类型（0：不良 1：报废）")
    @TableField("nc_type")
    private String ncType;

    @ApiModelProperty(value="不良工步bo")
    @TableField("nc_work_step_bo")
    private String ncWorkStepBo;

    @ApiModelProperty(value="责任人")
    @TableField("person_liable")
    private String personLiable;

    @ApiModelProperty(value="不良项目")
    @TableField("nc_project")
    private String ncProject;

    @ApiModelProperty(value="创建时间")
    @TableField("create_date")
    private Date createDate;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
