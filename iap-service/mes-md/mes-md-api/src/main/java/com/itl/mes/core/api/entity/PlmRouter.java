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

@TableName("plm_router_data")
@ApiModel(value="PlmRouter",description="PLM同步工艺路线数据存储表")
@Data
public class PlmRouter extends Model<PlmRouter> {
    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    private String id;

    @TableField("ROUTER")
    private String router;

    @TableField("ROUTER_NAME")
    private String routerName;

    @TableField("ROUTER_VERSION")
    private String routerVersion;

    @TableField("ITEM")
    private String item;

    @TableField("OPERATION_LIST")
    private String operationList;

    @TableField("CREATE_DATE")
    private Date createDate;

    @TableField("P_TYPE")
    private String pType;

    @TableField("CREATOR")
    private String creator;

    @TableField("S_MEMO")
    private String sMemo;

    @TableField("MACHT")
    private String macht;

    @TableField("DEVICE_NO")
    private String deviceNo;

    @TableField("DEVICE_NAME")
    private String deviceName;

    @TableField("PART_NO")
    private String partNo;

    @TableField("PART_NAME")
    private String partName;

    @TableField("PBOMPKG_ID")
    private String pbompkgId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
