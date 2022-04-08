package com.itl.mes.pp.api.entity.scheduleplan;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 日期类型表，用于排程计划的日期类型维护
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("p_date_type")
public class DateTypeEntity {

    @TableId(value = "ID",type = IdType.UUID)
    private String id;

    @TableField( "YEAR")
    private Integer year;

    @TableField( "MONTH")
    private Integer month;

    @TableField( "DAY")
    private Integer day;

    @TableField( "DATE")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date date;

    @TableField( "STATE")
    private Integer state;

}
