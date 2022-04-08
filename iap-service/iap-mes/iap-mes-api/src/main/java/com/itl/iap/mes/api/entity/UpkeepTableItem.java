package com.itl.iap.mes.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("m_repair_upkeep_table_item")
@ApiModel(value="UpkeepTableItem",description="保养单明细表")
public class UpkeepTableItem extends Model<UpkeepTable> {

    private static final long serialVersionUID = 434894764009734789L;

    @ApiModelProperty(value="id")
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value="项目名称")
    @TableField("item_name")
    private String itemName;

    @ApiModelProperty(value="项目描述")
    @TableField("remark")
    private String remark;

    @ApiModelProperty(value="操作")
    @TableField("operation")
    private String operation;

    @ApiModelProperty(value="备注")
    @TableField("comments")
    private String comments;

    @ApiModelProperty(value="保养单编号")
    @TableField("repairId")
    private String repairId;

    @ApiModelProperty(value="巡检结果")
    @TableField("inspection_result")
    private String inspectionResult;

    @ApiModelProperty(value="巡检评价")
    @TableField("inspection_evaluate")
    private String inspectionEvaluate;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
