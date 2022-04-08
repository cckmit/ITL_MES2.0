package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("me_sfc_assy")
@ApiModel(value="AssySfc",description="扣料表")
public class AssySfc extends Model<AssySfc> {
    private static final long serialVersionUID = -8384486979728802613L;

    @ApiModelProperty(value="SFC:SITE,SFC【PK】")
    @Length( max = 100 )
    @TableId(value = "ID", type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value="工厂【UK】")
    @Length( max = 32 )
    @TableField("SITE")
    private String site;

    @ApiModelProperty(value="SFC日志")
    @Length( max = 32 )
    @TableField("WIP_LOG_BO")
    private String wipLogBo;

    @ApiModelProperty(value="SFC编号")
    @Length( max = 50 )
    @TableField("SFC")
    private String sfc;

    @ApiModelProperty(value="追溯方式")
    @Length( max = 64 )
    @TableField("TRACE_METHOD")
    private String traceMethod;

    @ApiModelProperty(value="组件BO")
    @Length( max = 32 )
    @TableField("COMPONENT_BO")
    private String componentBo;

    @ApiModelProperty(value="装配SN")
    @Length( max = 100 )
    @TableField("ASSEMBLED_SN")
    private String assembledSn;

    @ApiModelProperty(value="装配数量")
    @Length( max = 100 )
    @TableField("QTY")
    private int qty;

    @ApiModelProperty(value="装配时间")
    @TableField("ASSY_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date assyTime;

    @ApiModelProperty(value="装配用户")
    @TableField("ASSY_USER")
    private String assyUser;

    @ApiModelProperty(value="移除时间")
    @TableField("REMOVED_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date removedTime;

    @ApiModelProperty(value="移除用户")
    @Length( max = 255 )
    @TableField("REMOVED_USER")
    private String removedUser;

    @ApiModelProperty(value="组件状态")
    @TableField("COMPONENT_STATE")
    private String componenetState;

    @ApiModelProperty(value="出站数量")
    @TableField("SFC_QTY")
    private int sfcQty;

    @Override
    protected Serializable pkVal() {
        return id = this.id;
    }
}
