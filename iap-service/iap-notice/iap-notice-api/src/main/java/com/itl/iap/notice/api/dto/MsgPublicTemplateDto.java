package com.itl.iap.notice.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 公共消息模板(包含邮件、系统消息、短信模板等，如果是短信模板类型，则需要关联短信消息模板表)(MsgPublicTemplate)DTO类
 *
 * @author liaochengdian
 * @date 2020-03-25
 * @since jdk1.8
 */
@Data
@Accessors(chain = true)
public class MsgPublicTemplateDto implements Serializable {
    private static final long serialVersionUID = -74363564114639589L;

    public static final String TYPE = "NX202006291474";

    public static final String NOTICE_TYPE_CODE = "NX202006296737";

    public static final String SERVICE_MODULE = "NX202006295875";

    public static final String MESSAGE_TYPE_CODE = "NX202006296492";

    //分页对象
    private Page page;
    
    /**
    * 主键
    */    private String id;
    /**
    * 消息类型主键
    */    private String msgTypeId;
    /**
     * 模板名称
     */
    private String name;
    /**
     * 模板编号
     */
    private String code;
    /**
     * 消息模板标题
     */
    private String title;

    /**
     * 消息类型快码
     */
    private String messageTypeCode;

    private String messageTypeName;

    /**
     * 通知类型快码
     */
    private Integer noticeTypeCode;

    private String noticeTypeName;

    /**
     * 通知是否启用(1.启用/0.不启用)
     */
    private Integer noticeEnabledFlag;
    /**
     * 系统通知内容模板
     */
    private String content;

    /**
     * 创建人
     */
    private String createName;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新人
     */
    private String updateName;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    /**
     * 业务模块快照编码
     */
    private Integer serviceModuleCode;

    private String serviceModuleName;

    /**
     * 模板类型
     */
    private Integer templateTypeCode;

    private String templateTypeName;
    /**
     * 签名
     */
    private String sign;

    /**
     * 短信编码
     */
    private String messageCode;

    /**
     * PC-跳转地址
     */
    private String pcUrl;
    /**
     * h5-跳转地址
     */
    private String hurl;
    /**
     * app-跳转地址
     */
    private String appUrl;
    /**
     * ios-跳转地址
     */
    private String iosUrl;

    List<String> typeCodes;
}