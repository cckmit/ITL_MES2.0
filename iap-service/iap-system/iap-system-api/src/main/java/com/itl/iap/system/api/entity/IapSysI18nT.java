package com.itl.iap.system.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.iap.common.base.model.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;

/**
 * 国际化实体类
 *
 * @author 李骐光
 * @date 2020-06-29
 * @since jdk1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("iap_sys_i18n_t")
public class IapSysI18nT extends BaseModel implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id")
    private String id;
    /**
     * 键
     */
    @TableField("i18n_key")
    private String i18nKey;
    /**
     * 值
     */
    @TableField("i18n_value")
    private String i18nValue;
    /**
     * 语言类型(中文:zh_CN/英文:en)
     */
    @TableField("language_type")
    private String languageType;
    /**
     * 客户端类型(pc-web:电脑浏览器/app)
     */
    @TableField("client_type")
    private String clientType;
    /**
     * 创建人
     */
    @TableField("creater")
    private String creater;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("create_date")
    private Date createDate;
    /**
     * 创建岗位
     */
    @TableField("create_org")
    private String createOrg;
    /**
     * 最后更新人
     */
    @TableField("last_update_by")
    private String lastUpdateBy;
    /**
     * 最后更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("last_update_date")
    private Date lastUpdateDate;

}
