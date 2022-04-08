package com.itl.iap.auth.dto;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * (IapAuthAccessTokenT)DTO类
 *
 * @author 汤俊
 * @date  2020-06-17 19:49:10
 * @since jdk1.8
 */
@Data
@Accessors(chain = true)
public class AuthAccessTokenDto implements Serializable {
    private static final long serialVersionUID = -80596710043476018L;

    /**
     * 分页对象
     */
    private Page page;

    /**
     * 主键ID
     */
    private String id;

    /**
     * 客户端ID
     */
    private String clientId;

    /**
     * 令牌ID
     */
    private String tokenId;

    /**
     * 令牌过期时间
     */
    private Long tokenExpired;

    /**
     * 身份验证ID
     */
    private String authenticationId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 令牌类型
     */
    private String tokenType;

    /**
     * refreshToken
     */
    private String refreshToken;

    /**
     * refreshToken 过期秒数
     */
    private Integer refreshTokenExpired;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 创建人
     */

    private String creater;

    /**
     * 最后更新人
     */
    private String lastUpdateBy;

    /**
     * 更新时间
     */
    private Date lastUpdateDate;
}