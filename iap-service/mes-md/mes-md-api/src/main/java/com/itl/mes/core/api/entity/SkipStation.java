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

@TableName("skip_station")
@ApiModel(value="SkipStation",description="跳站表")
@Data
public class SkipStation extends Model<SkipStation> {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="主键")
    @TableId(value = "ID", type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value="跳站之前的工艺路线bo")
    @TableField("OLD_ROUTER_BO")
    private String oldRouterBo;

    @ApiModelProperty(value="跳站之后的工艺路线bo（如果没有更改工艺路线，可以为空）")
    @TableField("NEW_ROUTER_BO")
    private String newRouterBo;

    @ApiModelProperty(value="跳站用户")
    @TableField("USER_BO")
    private String userBo;

    @ApiModelProperty(value="跳站的工序bo")
    @TableField("OPERATION_BO")
    private String operationBo;

    @ApiModelProperty(value="创建日期")
    @TableField("CREATE_DATE")
    private Date createDate;

    @ApiModelProperty(value="备注")
    @TableField("REMARKS")
    private String remarks;

    @ApiModelProperty(value="sfc")
    @TableField("SFC")
    private String sfc;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
