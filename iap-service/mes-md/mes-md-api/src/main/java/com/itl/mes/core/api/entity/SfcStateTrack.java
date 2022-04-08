package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sfc_state_track")
@ApiModel(value="SfcStateTrack",description="sfc生产状态跟踪表")
public class SfcStateTrack extends Model<SfcStateTrack> {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    @TableField("sfc")
    private String sfc;

    @TableField("operation_bo")
    private String operationBo;

    @TableField("state")
    private String state;

    @TableField("create_date")
    private Date createDate;

    @TableField("update_date")
    private Date updateDate;

    public static final String F_STATE = "3";//完成状态
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
