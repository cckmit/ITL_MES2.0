package com.itl.iap.system.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itl.iap.common.base.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 菜单实体类
 *
 * @author 谭强
 * @date 2020-06-20
 * @since jdk1.8
 */
@Data
@Accessors(chain = true)
@TableName("iap_sys_resource_t")
public class IapSysResourceT extends BaseModel {
    private static final long serialVersionUID = 257822105607733537L;

    /**
     * id
     */
    @TableId
    private String id;
    /**
     * 菜单名称
     */
    @TableField("resources_name")
    private String resourcesName;
    /**
     * 菜单编码
     */
    @TableField("resources_code")
    private String resourcesCode;
    /**
     * 菜单图标
     */
    @TableField("resources_icon")
    private String resourcesIcon;
    /**
     * 排序
     */
    @TableField("sort")
    private Short sort;
    /**
     * 父ID
     */
    @TableField("parent_id")
    private String parentId;
    /**
     * 路由地址
     */
    @TableField("router_path")
    private String routerPath;
    /**
     * 资源类型：0-菜单，2-详细页面，3-按钮，4-iframe
     */
    @TableField("resource_type")
    private Short resourceType;
    /**
     * 状态：0:不启用,1：启用
     */
    @TableField("state")
    private Short state;
    /**
     * 描述
     */
    @TableField("description")
    private String description;
    /**
     * 创建人
     */
    @TableField("creater")
    private String creater;
    /**
     * 创建时间
     */
    @TableField("create_date")
    private Date createDate;
    /**
     * 创建岗位
     */
    @TableField("create_org")
    private String createOrg;
    /**
     * 修改人
     */
    @TableField("last_update_by")
    private String lastUpdateBy;
    /**
     * 修改时间
     */
    @TableField("last_update_date")
    private Date lastUpdateDate;
    /**
     * 组件地址
     */
    @TableField("component")
    private String component;
    /**
     * 组件名称
     */
    @TableField("component_name")
    private  String componentName;
    /**
     *缓存(0-不缓存，1-缓存)
     */
    @TableField("cache")
    private  int cache;


}