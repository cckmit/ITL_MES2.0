package com.itl.mes.pp.api.entity.schedule;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liuchenghao
 * @date 2020/11/12 18:51
 */

/**
 * 手动排程：排程工单关联表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("p_schedule_shop_order")
public class ScheduleShopOrderEntity {


    @TableId(type = IdType.UUID)
    private String bo;


    @TableField("SHOP_ORDER_BO")
    private String shopOrderBo;



    @TableField("SCHEDULE_BO")
    private String scheduleBo;

}
