package com.itl.mes.pp.api.entity.schedule;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liuchenghao
 * @date 2020/11/11 16:20
 */

/**
 * 手动排程：排程产线关联表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("p_schedule_production_line")
public class ScheduleProductionLineEntity {


    @TableId(type = IdType.UUID)
    private String bo;

    @TableField("PRODUCTION_LINE_BO")
    private String productionLineBo;


    @TableField("STATION_BO")
    private String stationBo;


    @TableField("QUANTITY")
    private BigDecimal quantity;

    @TableField("START_DATE")
    private Date startDate;

    @TableField("END_DATE")
    private Date endDate;

    @TableField("SCHEDULE_BO")
    private String scheduleBo;


}
