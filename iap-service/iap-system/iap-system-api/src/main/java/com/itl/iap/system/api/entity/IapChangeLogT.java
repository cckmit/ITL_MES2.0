package com.itl.iap.system.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 操作变动日志(IapChangeLogT)实体类
 *
 * @author linjs
 * @since 2020-10-30 10:40:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("iap_change_log_t")
public class IapChangeLogT implements Serializable {
    private static final long serialVersionUID = -90216574613411933L;

    @TableId
    private String id;
    /**
    * 单据ID
    */
    @TableField("document_id")
    private String documentId;
    /**
    * 操作类型(1.新增；2.修改；3.删除)
    */
    @TableField("operator_type")
    private Integer operatorType;
    /**
    * 操作表ID
    */
    @TableField("table_id")
    private String tableId;
    /**
    * 操作表名称
    */
    @TableField("table_name")
    private String tableName;
    /**
    * 操作人员
    */
    @TableField("creater")
    private String creater;
    /**
    * 操作日期
    */
    @TableField("create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;
    /**
    * 更新操作人员
    */
    @TableField("last_update_by")
    private String lastUpdateBy;
    /**
    * 更新操作日期
    */
    @TableField("last_update_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date lastUpdateDate;
    /**
     * 变动日志详情列表
     */
    @TableField(exist = false)
    private List<IapChangeDetailLogT> iapChangeDetailLogDtoList;

}