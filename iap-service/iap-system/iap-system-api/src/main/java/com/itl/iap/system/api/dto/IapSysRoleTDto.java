package com.itl.iap.system.api.dto;

import java.util.Date;
import java.util.Date;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.google.common.collect.Lists;
import com.itl.iap.system.api.entity.IapSysUserT;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 角色dto
 *
 * @author 李虎
 * @date 2020-06-19
 * @since jdk1.8
 */
@Data
@Accessors(chain = true)
public class IapSysRoleTDto implements Serializable {
    private static final long serialVersionUID = 415867621801474519L;
    //分页对象
    private Page page;

    /**
     * 主键
     */
    private String id;
    /**
     * 角色名字
     */
    private String roleName;
    /**
     * 角色编码
     */
    private String roleCode;
    /**
     * 角色类型：0:内部角色，1:外部角色
     */
    private Short roleType;
    /**
     * 角色描述
     */
    private String description;
    /**
     * 排序
     */
    private Short sort;
    /**
     * 状态：0:正常状态，1:被锁定
     */
    private Short state;
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
     * 最后更新人
     */
    private String lastUpdateBy;
    /**
     * 最后更新时间
     */
    private Date lastUpdateDate;
    /**
     * 0：正常，1：删除
     */
    private Short delFlag;

    /**
     * 当前用户的所有权限
     */
    private List<IapSysAuthTDto> auths = Lists.newArrayList();

    /**
     * 该角色下的用户列表
     */
    private List<IapSysUserT> userList;

}