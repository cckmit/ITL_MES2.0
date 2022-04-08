package com.itl.iap.auth.dto;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * (IapAuthClientT)DTO类
 *
 * @author iAP
 * @since 2020-06-17 19:49:17
 */
@Data
@Accessors(chain = true)
public class AuthClientDto implements Serializable {
    private static final long serialVersionUID = -27859629105727191L;

    /**
     * 0 启用 1 禁用
     */
    public static final short STATE_0 = 0;
    public static final short STATE_1 = 1;

    /**
     * 分页对象
     */
    private Page page;

    /**
     * 主键ID
     */
    private String id;

    /**
     * 客户端名称
     */
    private String clientName;

    /**
     * 客户端ID
     */
    private String clientId;

    /**
     * 客户端密钥
     */
    private String clientSecret;

    /**
     * 客户端url
     */
    private String clientUri;

    /**
     * 客户端 icon url
     */
    private String clientIconUri;

    /**
     * 资源 ids
     */
    private String resourceIds;

    /**
     * 客户端授权范围
     */
    private String scope;

    /**
     * 客户端授权类型
     */
    private String grantTypes;

    /**
     * 跳转url
     */
    private String redirectUri;

    /**
     * 客户端状态
     */
    private Short state;

    /**
     * 客户端描述
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
     * 最后更新人
     */
    private String lastUpdateBy;

    /**
     * 最后修改时间
     */
    private Date lastUpdateDate;
}