package com.itl.iap.mes.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("m_repair_upkeep_execute_item")
public class UpkeepExecuteItem {
    private static final long serialVersionUID = -30729856515700265L;

    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 保养执行主键
     */
    @TableField("upkeepExecuteId")
    private String upkeepExecuteId;


    /**
     * 项目序号
     */
    @TableField("itemCode")
    private Integer itemCode;

    /**
     * 项目名称
     */
    @TableField("itemName")
    private String itemName;

    /**
     * 项目类型
     */
    @TableField("type")
    private Integer type;

    /**
     * 最大值
     */
    @TableField("maxNum")
    private Integer maxNum;

    /**
     * 最小值
     */
    @TableField("minNum")
    private Integer minNum;

    @TableField("itemValue")
    private String itemValue;


    @TableField(exist = false)
    private List<DataCollectionItemListing> dataCollectionItemListingList;


}
