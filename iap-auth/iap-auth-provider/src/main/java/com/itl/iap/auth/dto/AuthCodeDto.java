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
 * (IapAuthCodeT)DTO类
 *
 * @author iAP
 * @since 2020-06-17 19:49:17
 */
@Data
@Accessors(chain = true)
public class AuthCodeDto implements Serializable {
    private static final long serialVersionUID = -45041676752554951L;

    /**
     * 分页对象
     */
    private Page page;

    /**
     * 编码
     */
    private String code;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 客户端ID
     */
    private String clientId;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 状态
     */
    private int state;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 更新人
     */
    private String lastUpdateBy;

    /**
     * 最后更新时间
     */
    private Date lastUpdateDate;
}