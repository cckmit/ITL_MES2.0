package com.itl.iap.system.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 字典表主表实体类
 *
 * @author 李骐光
 * @date 2020-06-16
 * @since jdk1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("iap_dict_t")
public class IapDictT implements Serializable {
    private static final long serialVersionUID = -30729856515700265L;

    @TableId(type = IdType.UUID)
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
     * 字典封存(1封存 0 不封存)
     */
    @TableField("enabled")
    private Short enabled;

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
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 排序
     */
    @TableField("sort")
    private Short sort;
    @TableField("create_org")
    private String createOrg;
    @TableField("create_station")
    private String createStation;


    /**
     * 最后修改人
     */
    @TableField("last_update_by")
    private String lastUpdateBy;

    /**
     * 最后修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("last_update_date")
    private Date lastUpdateDate;

}
