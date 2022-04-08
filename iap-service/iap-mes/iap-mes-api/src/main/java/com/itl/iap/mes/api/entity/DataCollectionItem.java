package com.itl.iap.mes.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("m_data_collection_item")
public class DataCollectionItem {
    private static final long serialVersionUID = -30729856515700265L;

    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 项目序号
     */
    @TableField("itemNo")
    private Integer itemNo;

    /**
     * 项目名称
     */
    @TableField("itemName")
    private String itemName;

    /**
     * 项目描述
     */
    @TableField("remark")
    private String remark;

    /**
     * 项目类型
     */
    @TableField("itemType")
    private Integer itemType;

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

    /**
     * 主表id
     */
    @TableField("dataCollectionId")
    private String dataCollectionId;

    /**
     * 操作
     */
    @TableField(exist = false)
    private String operation;

    /**
     * 备注
     */
    @TableField(exist = false)
    private String comments;

    /**
     * 日
     */
    @TableField(exist = false)
    private String day;

    /**
     * 周
     */
    @TableField(exist = false)
    private String week;

    /**
     * 季度
     */
    @TableField(exist = false)
    private String quarter;

    @TableField(exist = false)
    private List<DataCollectionItemListing> dataCollectionItemListingList;

}
