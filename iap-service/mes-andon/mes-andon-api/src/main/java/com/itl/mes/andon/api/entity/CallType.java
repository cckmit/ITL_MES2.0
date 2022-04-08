package com.itl.mes.andon.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@TableName("andon_call_type")
@ApiModel(value = "call_type", description = "安灯呼叫类型")
public class CallType implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "状态（已启用0，已禁用1）")
    @Length(max = 1)
    @TableField("state")
    private String state;

    @ApiModelProperty(value = "建档日期")
    @TableField("create_date")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date createDate;

    @ApiModelProperty(value = "建档人")
    @TableField("create_user")
    private String createUser;

    @ApiModelProperty(value = "关联车间")
    @TableField("workshop_bo")
    private String workshopBo;

    @ApiModelProperty(value = "关联车间")
    @TableField("workshop_name")
    private String workshopName;


    @ApiModelProperty(value = "安灯类型名称")
    @TableField("andon_type_name")
    private String andonTypeName;

    @ApiModelProperty(value = "安灯类型编号")
    @TableField("andon_type")
    private String andonType;
}
