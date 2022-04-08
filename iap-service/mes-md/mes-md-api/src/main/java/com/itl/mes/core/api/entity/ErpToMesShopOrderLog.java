package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("erp_to_mes_shop_order_log")
public class ErpToMesShopOrderLog {

    @TableField("shop_order")
    private String shopOrder;
    @TableField("info")
    private String info;
    @TableField("create_time")
    private Date createTime;
}
