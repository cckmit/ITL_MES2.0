package com.itl.iap.mes.api.entity.demand;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liuchenghao
 * @date 2020/11/5 15:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("m_demand_merge_split")
public class DemandMergeSplitEntity {


    @TableId( "ID")
    private String id;

    @TableField("DEMAND_NO")
    private String demandNo;

    @TableField("MERGE_SPLIT_DEMAND_NO")
    private String mergeSplitDemandNo;


}
