package com.itl.iap.mes.api.entity;

import cn.hutool.db.DaoTemplate;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("m_data_collection_type")
public class DataCollectionType {
    private static final long serialVersionUID = -30729856515700265L;

    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 工厂代码
     */
    @TableField("site")
    private String site;

    /**
     * 数据收集类型
     */
    @TableField("type")
    private String type;

    /**
     * 类型描述
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

    @TableField("CREATE_DATE")
    private Date createDate;
}
