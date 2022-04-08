package com.itl.iap.system.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 字典表详情表实体类
 *
 * @author 李骐光
 * @date 2020-06-16
 * @since jdk1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("iap_dict_item_t")
public class IapDictItemT implements Serializable {
    private static final long serialVersionUID = -73380738638203815L;

    @TableId
    private String id;
    /**
     * 字典编码
     */
    @TableField("code")
    private String code;
    /**
     * 字典名称
     */
    @TableField("name")
    private String name;
    /**
     * 封存(1已启用 0 未启用)
     */
    @TableField("enabled")
    private Short enabled;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 主表id
     */
    @TableField("iap_dict_id")
    private String iapDictId;

    /**
     * 排序
     */
    @TableField("sort")
    private Short sort;

    /**
     * 创建人
     */
    @TableField("creater")
    private String creater;

    /**
     * 最后更新人
     */
    @TableField("last_update_by")
    private String lastUpdateBy;

    /**
     * 创建时间
     */
    @TableField("create_date")
    private Date createDate;

    /**
     * 更新时间
     */
    @TableField("last_update_date")
    private Date lastUpdateDate;

    /**
     * 字典键值
     */
    @TableField("key_value")
    private String keyValue;
}
