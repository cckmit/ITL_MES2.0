package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sfc_skip_before_operation")
public class SfcSkipBeforeOperation extends Model<SfcSkipBeforeOperation> {
    private static final long serialVersionUID = 1L;

    @TableField("sfc")
    private String sfc;
    @TableField("operation_bo")
    private String operationBo;
    @TableField("create_date")
    private Date createDate;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
