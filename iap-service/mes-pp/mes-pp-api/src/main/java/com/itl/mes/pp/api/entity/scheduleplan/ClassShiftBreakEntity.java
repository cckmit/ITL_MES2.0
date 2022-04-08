package com.itl.mes.pp.api.entity.scheduleplan;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 班次休息表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("p_class_shift_break")
public class ClassShiftBreakEntity {


    @TableId(value = "ID",type = IdType.UUID)
    private String id;

    @TableField( "CLASS_FREQUENCY_ID")
    private String classFrequencyId;


    @TableField( "REST_TYPE")
    private String restType;


    @TableField( "START_DATE_STR")
    private String startDateStr;


    @TableField( "END_DATE_STR")
    private String endDateStr;

    @TableField( "SORT_MARK")
    private Integer sortMark;

}
