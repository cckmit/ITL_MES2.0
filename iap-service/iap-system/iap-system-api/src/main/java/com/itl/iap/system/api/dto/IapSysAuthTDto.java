package com.itl.iap.system.api.dto;

import java.util.Date;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 权限表dto
 *
 * @author 李虎
 * @date 2020-06-19
 * @since jdk1.8
 */
@Data
@Accessors(chain = true)
public class IapSysAuthTDto implements Serializable {
    private static final long serialVersionUID = 123250230159623466L;
    //分页对象
    private Page page;

    /**
     * 主键
     */
    private String id;
    /**
     * 权限名字
     */
    private String authName;
    /**
     * 父id
     */
    private String parentId;
    /**
     * 权限编码
     */
    private String authCode;
    /**
     * 权限类型：0:内部权限，1:外部权限
     */
    private Short authType;
    /**
     * 权限描述
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date lastUpdateDate;
    /**
     * 0：正常，1：删除
     */
    private Short delFlag;
    /**
     * 子权限，自关联
     */
    private List<IapSysAuthTDto> children = Lists.newArrayList();
    /**
     * 权限名字，前端树用
     */
    private String label;

    /**
     * 权限对应的资源列表
     */
    private List<IapSysResourceTDto> resources = Lists.newArrayList();

}