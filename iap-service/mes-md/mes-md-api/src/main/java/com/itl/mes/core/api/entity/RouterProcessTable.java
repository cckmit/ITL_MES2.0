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
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("m_router_process_table")
@ApiModel(value = "router_process_table", description = "工艺路线表")
public class RouterProcessTable extends Model<RouterProcessTable> {

    private static final long serialVersionUID = -5893702987265672311L;

    @ApiModelProperty(value="工艺路线bo")
    @TableField("router_bo")
    private String routerBo;

    @TableField("operation_bo")
    @ApiModelProperty(value = "工序BO")
    private String operationBo;

    @TableField("position_flag")
    @ApiModelProperty(value = "工序位置（顺序）")
    private int positionFlag;

    @TableField("parallel_flag")
    @ApiModelProperty(value = "并行标识")
    private int parallelFlag;

    @TableField("create_date")
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;

    @TableField("update_date")
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateDate;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
