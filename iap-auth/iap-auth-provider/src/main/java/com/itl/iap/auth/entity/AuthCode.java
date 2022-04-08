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
 * (IapAuthCodeT)实体类
 *
 * @author iAP
 * @since 2020-06-17 19:49:17
 */
@Data
@Accessors(chain = true)
@TableName("iap_auth_code_t")
public class AuthCode extends BaseModel {
    private static final long serialVersionUID = -72652193039147264L;

    /**
     * 0:正常
     * 1：被锁定
     */
    public static final short STATE_1 = 1;
    public static final short STATE_0 = 0;

    /**
     * 编码
     */
    @TableId
    @TableField("code")
    private String code;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private String userId;

    /**
     * 客户端ID
     */
    @TableField("client_id")
    private String clientId;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("create_date")
    private Date createDate;

    /**
     * 状态
     */
    @TableField("state")
    private int state;

    /**
     * 创建人
     */
    @TableField("creater")
    private String creater;

    /**
     * 更新人
     */
    @TableField("last_update_by")
    private String lastUpdateBy;

    /**
     * 最后更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("last_update_date")
    private Date lastUpdateDate;

}