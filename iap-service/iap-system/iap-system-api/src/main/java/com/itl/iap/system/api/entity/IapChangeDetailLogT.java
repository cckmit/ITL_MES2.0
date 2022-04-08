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

/**
 * 操作变动明细日志(IapChangeDetailLogT)实体类
 *
 * @author linjs
 * @since 2020-10-30 11:06:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("iap_change_detail_log_t")
public class IapChangeDetailLogT  implements Serializable {
    private static final long serialVersionUID = -88855609200243723L;

    @TableId
    private String id;
    /**
    * 操作变动日志ID
    */
    @TableField("change_log_id")
    private String changeLogId;
    /**
    * 字段名
    */
    @TableField("field_code")
    private String fieldCode;
    /**
    * 字段描述
    */
    @TableField("field_name")
    private String fieldName;
    /**
    * 旧值
    */
    @TableField("old_value")
    private String oldValue;
    /**
    * 新值
    */
    @TableField("new_value")
    private String newValue;
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

}