package com.itl.iap.auth.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.iap.common.base.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * (IapAuthAccessTokenT)实体类
 *
 * @author iAP
 * @since 2020-06-17 19:49:09
 */
@Data
@Accessors(chain = true)
@TableName("iap_auth_access_token_t")
public class AuthAccessToken extends BaseModel {

    private static final long serialVersionUID = -61017762676042836L;

    /**
     * 主键ID
     */
    @TableId("id")
    @TableField("id")
    private String id;

    /**
     * 客户端ID
     */
    @TableField("client_id")
    private String clientId;

    /**
     * 令牌ID
     */
    @TableField("token_id")
    private String tokenId;

    /**
     * 令牌过期时间
     */
    @TableField("token_expired_seconds")
    private Long tokenExpired;

    /**
     * 身份验证ID
     */
    @TableField("authentication_id")
    private String authenticationId;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private String userId;

    /**
     * 令牌类型
     */
    @TableField("token_type")
    private String tokenType;

    /**
     * refreshToken
     */
    @TableField("refresh_token")
    private String refreshToken;

    /**
     * refreshToken 过期秒数
     */
    @TableField("refresh_token_expired_seconds")
    private Integer refreshTokenExpired;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("create_date")
    private Date createDate;

    /**
     * 创建人
     */
    @TableField("creater")
    private String creater;

    /**
     * 最后更新人
     */
    @TableField("last_update_by")
    private String lastUpdateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("last_update_date")
    private Date lastUpdateDate;

    /**
     * 判断刷新token是否过期
     *
     * @return
     */
    public boolean refreshTokenExpired() {
        final long time = createDate.getTime() + (this.refreshTokenExpired);
        return time < System.currentTimeMillis();
    }

    /**
     * 判断token是否过期
     *
     * @return
     */
    public boolean tokenExpired() {
        final long time = createDate.getTime() + (this.tokenExpired);
        return time < System.currentTimeMillis();
    }
}