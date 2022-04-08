package com.itl.mes.pp.api.entity.scheduleplan;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 班次表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("p_class_frequency")
public class ClassFrequencyEntity {


    @TableId(value = "ID",type = IdType.UUID)
    private String id;

    @TableField( "CLASS_SYSTEM_ID")
    private String classSystemId;

    @TableField( "CODE")
    private String code;

    @TableField( "NAME")
    private String name;

    @TableField( "START_DATE_STR")
    private String startDateStr;

    @TableField( "END_DATE_STR")
    private String endDateStr;

    @TableField( "IS_NEXT_DAY")
    private Integer isNextDay;

}
