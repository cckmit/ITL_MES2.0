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
 * 岗位用户中间表
 *
 * @author 谭强
 * @date 2020-06-24
 * @since jdk1.8
 */
@Data
@Accessors(chain = true)
public class IapSysPositionUserTDto implements Serializable {
    private static final long serialVersionUID = 965946267488299179L;
    //分页对象
    private Page page;

    /**
     * 主键
     */
    private String id;
    /**
     * 用户
     */
    private String userId;
    /**
     * 岗位
     */
    private String positionId;
    /**
     * 创建者
     */
    private String creater;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 创建组织
     */
    private String createOrg;
    /**
     * 最后修改人
     */
    private String lastUpdateBy;
    /**
     * 最后修改时间
     */
    private Date lastUpdateDate;
}