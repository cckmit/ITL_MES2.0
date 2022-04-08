package com.itl.mes.pp.api.entity;

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
 * @author xuejianhui
 * @date 2020/11/11 16:20
 */

/**
 * 工单绑定表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("m_work_order_binding")
public class WorkOrderBinding {


    @TableId(type = IdType.UUID)
    private String id;

    /*
    批号（标识）
     */
    @TableField("no")
    private String no;


    /*
    工单bo
     */
    @TableField("bo")
    private String bo;


    /*
    排序
     */
    @TableField("sort")
    private Integer sort;


    @TableField("site")
    private String site;


}
