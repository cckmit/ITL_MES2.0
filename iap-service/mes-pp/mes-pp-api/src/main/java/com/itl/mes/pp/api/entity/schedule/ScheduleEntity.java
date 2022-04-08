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
 * 手动排程表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("p_schedule")
public class ScheduleEntity {


    @TableId(type = IdType.UUID)
    private String bo;

    @TableField("SCHEDULE_NO")
    private String scheduleNo;


    @TableField("ITEM_BO")
    private String itemBo;


    @TableField("BOM_BO")
    private String bomBo;


    @TableField("WORKSHOP_BO")
    private String workshopBo;

    @TableField("SCHEDULE_TYPE")
    private String scheduleType;

    @TableField("STATE")
    private Integer state;

    @TableField("ROUTER_BO")
    private String routerBo;


    @TableField("CONTROL_STATE")
    private Integer controlState;

    @TableField("PRIORITY")
    private String priority;

    @TableField("START_DATE")
    private Date startDate;

    @TableField("END_DATE")
    private Date endDate;

    @TableField("CREATE_DATE")
    private Date createDate;

    @TableField("CREATE_USER")
    private String createUser;

    @TableField("MODIFY_DATE")
    private Date modifyDate;


    @TableField("MODIFY_USER")
    private String modifyUser;


    @TableField("QUANTITY")
    private BigDecimal quantity;

    @TableField("SITE")
    private String site;


}
