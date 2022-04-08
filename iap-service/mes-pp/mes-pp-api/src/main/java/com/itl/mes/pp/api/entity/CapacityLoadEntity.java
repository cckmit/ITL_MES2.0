package com.itl.mes.pp.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @auth liuchenghao
 * @date 2021/1/13
 */
@Data
@TableName("p_capacity_load")
public class CapacityLoadEntity {


    @TableId(value = "ID",type = IdType.UUID)
    private String id;


    @TableField( "PRODUCT_LINE_BO")
    private String productLineBo;


    @TableField( "DATE")
    private Date date;

    @TableField( "AVAILABLE_WORK_HOUR")
    private Double availableWorkHour;

    @TableField( "ARRANGED_WORK_HOUR")
    private Double arrangedWorkHour;

}
