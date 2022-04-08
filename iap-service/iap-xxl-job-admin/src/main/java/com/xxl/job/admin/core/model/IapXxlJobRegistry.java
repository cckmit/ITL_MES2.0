package com.xxl.job.admin.core.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 执行器注册表，维护在线的执行器和调度中心机器地址信息
 *
 * @author xuxueli
 * @date 2016-5-19
 * @since jdk1.8
 */
@Accessors(chain = true)
@TableName("xxl_job_registry")
public class IapXxlJobRegistry {

    /**
     * 主键ID
     */
    @TableId
    private int id;

    /**
     * 注册分组
     */
    @TableField("registry_group")
    private String registryGroup;

    /**
     * 注册键
     */
    @TableField("registry_key")
    private String registryKey;

    /**
     * 注册值
     */
    @TableField("registry_value")
    private String registryValue;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegistryGroup() {
        return registryGroup;
    }

    public void setRegistryGroup(String registryGroup) {
        this.registryGroup = registryGroup;
    }

    public String getRegistryKey() {
        return registryKey;
    }

    public void setRegistryKey(String registryKey) {
        this.registryKey = registryKey;
    }

    public String getRegistryValue() {
        return registryValue;
    }

    public void setRegistryValue(String registryValue) {
        this.registryValue = registryValue;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
