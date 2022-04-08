package com.xxl.job.admin.core.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 执行器信息表，维护任务执行器信息
 *
 * @author Created by xuxueli
 * @date 2016-09-30
 * @since jdk1.8
 */
@Accessors(chain = true)
@TableName("xxl_job_group")
public class IapXxlJobGroup {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 执行器AppName
     */
    @TableField("app_name")
    private String appname;

    /**
     * 执行器名称
     */
    @TableField("title")
    private String title;

    /**
     * 执行器地址类型(0:自动注册, 1:手动录入)
     */
    @TableField("address_type")
    private int addressType;

    /**
     * 执行器地址列表，多地址逗号分隔(手动录入)
     */
    @TableField("address_list")
    private String addressList;

    /**
     * 执行器地址列表(系统注册)
     */
    private List<String> registryList;

    public List<String> getRegistryList() {
        if (addressList != null && addressList.trim().length() > 0) {
            registryList = new ArrayList<String>(Arrays.asList(addressList.split(",")));
        }
        return registryList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAddressType() {
        return addressType;
    }

    public void setAddressType(int addressType) {
        this.addressType = addressType;
    }

    public String getAddressList() {
        return addressList;
    }

    public void setAddressList(String addressList) {
        this.addressList = addressList;
    }

}
