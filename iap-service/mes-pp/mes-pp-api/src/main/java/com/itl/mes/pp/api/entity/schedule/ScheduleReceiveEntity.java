package com.itl.mes.pp.api.entity.schedule;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liuchenghao
 * @date 2020/11/16 10:57
 */

/**
 * 手动排程：派工接收表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("p_schedule_receive")
@NotNull
public class ScheduleReceiveEntity {

    @TableId(type = IdType.UUID)
    private String bo;


    @TableField("SHOP_ORDER_BO")
    private String shopOrderBo;


    @TableField("SCHEDULE_BO")
    private String scheduleBo;


    @TableField("SCHEDULE_QTY")
    @NotNull
    private BigDecimal scheduleQty;

    @TableField("RECEIVE_QTY")
    @NotNull
    private BigDecimal receiveQty;


    @TableField("STATE")
    private Integer state = 1;


    @TableField("RECEIVE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date receiveDate;


    @TableField("CREATE_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;

    @TableField("CREATE_USER")
    private String createUser;

    @TableField("MODIFY_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date modifyDate;


    @TableField("MODIFY_USER")
    private String modifyUser;

    @TableField("PRODUCTION_LINE_BO")
    private String productionLineBo;

    @TableField("STATION_BO")
    private String stationBo;

    @TableField("WORK_SHOP_BO")
    private String workShopBo;
}
