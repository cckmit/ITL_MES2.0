package com.itl.iap.mes.api.entity.demand;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author liuchenghao
 * @date 2020/11/5 11:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("m_demand")
public class DemandEntity {


    @TableId( "ID")
    private String id;


    @TableField("DEMAND_NO")
    private String demandNo;


    @TableField("DEMAND_TYPE")
    private String demandType;


    @TableField("CUSTOMER_BO")
    private String customerBo;

    @TableField("MATERIAL_NO")
    private String materialNo;

    @TableField("ORIGINAL_NUMBER")
    private Integer originalNumber;

    @TableField("NOW_NUMBER")
    private Integer nowNumber;

    @TableField("UNIT")
    private String unit;

    @TableField("PLAN_DATE")
    private Date planDate;

    @TableField("IS_EXPEDITED")
    private Integer isExpedited;


    @TableField("IS_DELETE")
    private Integer isDelete;






}
