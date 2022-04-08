package com.itl.iap.mes.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("m_data_collection_item_listing")
public class DataCollectionItemListing {
    private static final long serialVersionUID = -30729856515700265L;

    @TableId(type = IdType.UUID)
    private String id;

    /*
    序号
     */
    @TableField("serial")
    private Integer serial;

    /*
    键
     */
    @TableField("keyValue")
    private String keyValue;

    /*
    文本
     */
    @TableField("textValue")
    private String textValue;

    /*
    主表id
     */
    @TableField("dataCollectionItemId")
    private String dataCollectionItemId;

}
