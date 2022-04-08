package com.itl.iap.data.source.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * (iap_common_datasource_t) 表实体类
 *
 * @author tanq
 * @date 2020-06-18
 * @since jdk1.8
 */
@Data
@Accessors(chain = true)
@TableName("iap_common_datasource_t")
@ToString
public class DataSource {
    /**
     * 主键ID
     */
    @TableId
    private String id;

    /**
     * 数据编码 唯一 不可重复
     */
    @TableField("datasource_code")
    private String datasourceCode;

    /**
     * 数据库地址
     */
    @TableField("database_url")
    private String databaseUrl;

    /**
     * 数据库账号
     */
    @TableField("user_name")
    private String userName;

    /**
     * 数据库密码
     */
    @TableField("pass_word")
    private String passWord;

    /**
     * 数据库链接类型
     */
    @TableField("database_type")
    private String databaseType;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField("last_update_date")
    private Date lastUpdateDate;
}


