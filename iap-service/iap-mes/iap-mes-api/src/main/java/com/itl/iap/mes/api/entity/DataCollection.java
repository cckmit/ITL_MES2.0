package com.itl.iap.mes.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("m_data_collection")
public class DataCollection {
    private static final long serialVersionUID = -30729856515700265L;

    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 数据收集类型id
     */
    @TableField("dataCollectionTypeId")
    private String dataCollectionTypeId;

    /**
     * 数据收集名称
     */
    @TableField("name")
    private String name;

    /**
     * 描述
     */
    @TableField("remark")
    private String remark;

    /**
     * 状态
     */
    @TableField("state")
    private Integer state;

    @TableField(exist = false)
    private String stateName;

    /**
     * 工厂id
     */
    @TableField("siteId")
    private String siteId;
}
