package com.itl.iap.notice.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.iap.common.base.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 公共消息模板(包含邮件、系统消息、短信模板等，如果是短信模板类型，则需要关联短信消息模板表)(MsgPublicTemplate)实体类
 *
 * @author liaochengdian
 * @date 2020-03-25
 * @since jdk1.8
 */
@Data
@Accessors(chain = true)
@TableName("msg_public_template")
public class MsgPublicTemplate extends BaseModel {
    private static final long serialVersionUID = -31964387257281825L;
    
    /**
    * 主键
    */
    private String id;
    /**
    * 消息类型主键
    */
    @TableField("msg_type_id")
    private String msgTypeId;
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
    @TableField("message_type_code")
    private String messageTypeCode;

    /**
    * 通知类型快码
    */
    @TableField("notice_type_code")
    private Integer noticeTypeCode;

    /**
    * 通知是否启用(1.启用/0.不启用)
    */
    @TableField("notice_enabled_flag")
    private Integer noticeEnabledFlag;
    /**
    * 系统通知内容模板
    */
    @TableField("sys_notice_content")
    private String content;

    /**
    * 创建人
    */
    @TableField("create_name")
    private String createName;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale = "zh", timezone = "GMT+8")
    @TableField("create_time")
    private Date createTime;
    /**
    * 更新人
    */
    @TableField("update_name")
    private String updateName;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale = "zh", timezone = "GMT+8")
    @TableField("update_time")
    private Date updateTime;

    /**
     * 业务模块快照编码
     */
    @TableField("service_module_code")
    private Integer serviceModuleCode;

    /**
     * 模板类型快照编码
     */
    @TableField("template_type_code")
    private Integer templateTypeCode;
    /**
     * 签名
     */
    private String sign;

    /**
     * 短信编码
     */
    @TableField("message_code")
    private String messageCode;

    /**
     * PC-跳转地址
     */
    @TableField("pc_url")
    private String pcUrl;
    /**
     * h5-跳转地址
     */
    @TableField("h_url")
    private String hurl;
    /**
     * app-跳转地址
     */
    @TableField("app_url")
    private String appUrl;
    /**
     * ios-跳转地址
     */
    @TableField("ios_url")
    private String iosUrl;

    /** 发送短信时，保存用户信息**/
    @TableField(exist = false)
    String userId;
    @TableField(exist = false)
    String userName;
    @TableField(exist = false)
    String businessId;
}