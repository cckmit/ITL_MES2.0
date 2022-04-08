package com.itl.iap.system.api.dto;

import java.util.Date;
import java.util.Date;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户菜单dto
 *
 * @author 谭强
 * @date 2020-06-22
 * @since jdk1.8
 */
@Data
@Accessors(chain = true)
public class IapSysAuthResourceTDto implements Serializable {
    private static final long serialVersionUID = -11627643852576460L;
    //分页对象
    private Page page;

    /**
     * 主键
     */
    private String id;
    /**
     * 资源菜单id
     */
    private String resourceId;
    /**
     * 权限id
     */
    private String authId;
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
}