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
 * (IapAuthClientT)实体类
 *
 * @author iAP
 * @since 2020-06-17 19:49:17
 */
@Data
@Accessors(chain = true)
@TableName("iap_auth_client_t")
public class AuthClient extends BaseModel {
    private static final long serialVersionUID = -94527156979585616L;

    /**
     * 主键ID
     */
    @TableId
    @TableField("id")
    private String id;

    /**
     * 客户端名称
     */
    @TableField("client_name")
    private String clientName;

    /**
     * 客户端ID
     */
    @TableField("client_id")
    private String clientId;

    /**
     * 客户端密钥
     */
    @TableField("client_secret")
    private String clientSecret;

    /**
     * 客户端url
     */
    @TableField("client_uri")
    private String clientUri;

    /**
     * 客户端 icon url
     */
    @TableField("client_icon_uri")
    private String clientIconUri;

    /**
     * 资源 ids
     */
    @TableField("resource_ids")
    private String resourceIds;

    /**
     * 客户端授权范围
     */
    @TableField("scope")
    private String scope;

    /**
     * 客户端授权类型
     */
    @TableField("grant_types")
    private String grantTypes;

    /**
     * 跳转url
     */
    @TableField("redirect_uri")
    private String redirectUri;

    /**
     * 客户端状态
     */
    @TableField("state")
    private Short state;

    /**
     * 客户端描述
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
     * 最后更新人
     */
    @TableField("last_update_by")
    private String lastUpdateBy;

    /**
     * 最后修改时间
     */
    @TableField("last_update_date")
    private Date lastUpdateDate;

}