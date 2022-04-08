package com.itl.iap.system.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * Oauth2客户端
 *
 * @author 谭强
 * @date 2020-06-17
 * @since jdk1.8
 */
@Data
@Accessors(chain = true)
public class AuthClientDto implements Serializable {
    private static final long serialVersionUID = -27859629105727191L;
    //分页对象
    private Page page;

    /**
     * 主键
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
     * 客户端安全key
     */
    private String clientSecret;
    /**
     * 客户端url
     */
    private String clientUri;
    /**
     * 客户端icon url
     */
    private String clientIconUri;
    /**
     * 资源清单
     */
    private String resourceIds;
    /**
     * 客户端范围
     */
    private String scope;
    /**
     * 客户端授权模式
     */
    private String grantTypes;
    /**
     * 跳转url
     */
    private String redirectUri;
    /**
     * 状态：0 正常，1 被锁定
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
     * 最后更新人
     */
    private String lastUpdateBy;
    /**
     * 最后更新时间
     */
    private Date lastUpdateDate;
}