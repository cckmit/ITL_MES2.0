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
import java.util.Date;

@Data
@TableName("stem_operation_config")
@ApiModel(value="StemOperationConfig",description="线圈工序配置")
public class StemOperationConfig extends Model<StemOperationConfig> {

    @ApiModelProperty(value="唯一标识")
    @TableId(value = "ID", type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value="工序BO")
    @TableField("operation_bo")
    private String operationBo;

    @ApiModelProperty(value="创建时间")
    @TableField("create_date")
    private Date createDate;

    @ApiModelProperty(value="创建人")
    @TableField("create_user")
    private String createUser;

    @ApiModelProperty(value="代码类型")
    @TableField("code_rule_type")
    private String codeRuleType;

    @ApiModelProperty(value="标签ID")
    @TableField("label_id")
    private String labelId;

    @ApiModelProperty(value="工序名称")
    @TableField(exist = false)
    private String operationName;

    @ApiModelProperty(value="工序编码")
    @TableField(exist = false)
    private String operation;

    @ApiModelProperty(value="工序BO")
    @TableField(exist = false)
    private String bo;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
