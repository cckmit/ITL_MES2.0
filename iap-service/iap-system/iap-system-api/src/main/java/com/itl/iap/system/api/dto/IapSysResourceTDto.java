package com.itl.iap.system.api.dto;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单dto
 *
 * @author 谭强
 * @date 2020-06-20
 * @since jdk1.8
 */
@Data
@Accessors(chain = true)
public class IapSysResourceTDto implements Serializable {
    private static final long serialVersionUID = 391721357635502166L;
    /**
     * 0:菜单 2:详细页面 3:按钮
     */
    private static final Short RESOURCE_TYPE0 = 0;
    private static final Short RESOURCE_TYPE1 = 1;
    private static final Short RESOURCE_TYPE2 = 2;
    /**
     * 分页对象
     */
    private Page page;
    /**
     * id
     */
    private String id;
    /**
     * 菜单名称
     */
    private String resourcesName;
    /**
     * 菜单编码
     */
    private String resourcesCode;
    /**
     * 菜单图标
     */
    private String resourcesIcon;
    /**
     * 排序
     */
    private Short sort;
    /**
     * 父ID
     */
    private String parentId;
    /**
     * 路由地址
     */
    private String routerPath;
    /**
     * 资源类型：0-菜单，2-详细页面，3-按钮，4-iframe
     */
    private Short resourceType;
    /**
     * 状态：0:正常状态,1：不展示
     */
    private Short state;
    /**
     * 描述
     */
    private String description;
    /**
     * 创建人
     */
    private String creater;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 创建岗位
     */
    private String createOrg;
    /**
     * 修改人
     */
    private String lastUpdateBy;
    /**
     * 修改时间
     */
    private Date lastUpdateDate;
    /**
     * 组件地址
     */
    private String component;
    /**
     * 组件名称
     */
    private  String componentName;
    /**
     * 子组件
     */
    private List<IapSysResourceTDto> iapSysResourceTDtos;
    /**
     *缓存
     */
    @TableField("cache")
    private  int cache;
}