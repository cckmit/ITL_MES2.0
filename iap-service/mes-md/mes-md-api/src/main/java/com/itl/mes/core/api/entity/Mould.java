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

import java.io.Serializable;
import java.util.Date;

@TableName("m_dy_mould_collection")
@ApiModel(value="Mould",description="模具表")
@Data
public class Mould extends Model<Mould> {

    private static final long serialVersionUID = 6165992960184607080L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "BO",type = IdType.INPUT)
    private String Bo;

    @ApiModelProperty(value = "模具编码(设备编码)")
    @TableField(value = "MOULD")
    private String mould;

    @ApiModelProperty(value = "模具名称")
    @TableField(value = "MOULD_NAME")
    private String mouldName;

    @ApiModelProperty(value = "车间BO")
    @TableField(value = "WORK_SHOP_BO")
    private String workShopBo;

    @ApiModelProperty(value = "工序BO")
    @TableField(value = "OPERATION_BO")
    private String operationBo;

    @ApiModelProperty(value = "产品（物料）BO")
    @TableField(value = "ITEM_BO")
    private String itemBo;

    @ApiModelProperty(value = "领用人员（realName）")
    @TableField(value = "COLLET_USER")
    private String collectUser;

    @ApiModelProperty(value = "归还人员（realName）")
    @TableField(value = "RETURN_USER")
    private String returnUser;

    @ApiModelProperty(value = "领用时间")
    @JsonFormat(
        pattern = "yyyy-MM-dd",
        timezone = "GMT+8"
    )
    @TableField(value = "COLLET_DATE")
    private Date collectDate;

    @ApiModelProperty(value = "归还时间")
    @JsonFormat(
        pattern = "yyyy-MM-dd",
        timezone = "GMT+8"
    )
    @TableField(value = "RETURN_DATE")
    private Date returnDate;

    @ApiModelProperty(value = "状态（0待归还，1已完成)")
    @TableField(value = "STATE")
    private String state;

    @ApiModelProperty(value = "车间名称")
    @TableField(exist = false)
    private String workShopName;

    @ApiModelProperty(value = "工序名称")
    @TableField(exist = false)
    private String operationName;

    @ApiModelProperty(value = "物料名称")
    @TableField(exist = false)
    private String itemName;

    @ApiModelProperty(value = "物料编码")
    @TableField(exist = false)
    private String item;

    @ApiModelProperty(value = "车间")
    @TableField(exist = false)
    private String workShop;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
