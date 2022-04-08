package com.itl.iap.notice.api.dto;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.iap.notice.api.pojo.BaseRequestPojo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息发送记录表(MsgSendRecord)DTO类
 *
 * @author liaochengdian
 * @date  2020-03-25
 * @since jdk1.8
 */

@Data
@Accessors(chain = true)
public class  MsgSendRecordDto extends BaseRequestPojo implements Serializable {
    private static final long serialVersionUID = 264535704478669687L;

    public static final String PAGE_CODE = "info-receive";

    public static final String STATUS = "NX202006291405";

    public static final String SEND_TYPE = "NX202006296492";

    public static final String SERVICE_MODULE = "NX202006295875";

    Page page;

    private String id;
    /**
    * 通知渠道
    */
    private Integer sendType;
    private String sendTypeName;
    /**
    * 发送状态(1.发送中/2.发送成功/3.发送失败)
    */    private Integer status;

    private String statusName;
    /**
    * 发送主题
    */    private String title;
    /**
    * 发送内容
    */    private String content;
    /**
     * 公共消息模板Id
     */
    private String msgPublicTemplateId;

    private String msgPublicTemplateCode;

    private String msgPublicTemplateName;
    /**
    * 通知类型
    */    private Integer noticeTypeCode;


    private String noticeTypeName;
    /**
    * 消息类型
    */    private String msgType;
    /**
    * 接收人主键
    */    private String receiverUid;
    /**
    * 接收人姓名
    */    private String receiverName;
    /**
    * 发送时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date sendTime;
    /**
    * 已读状态(1.已读/0.未读)
    */

    private Integer readFlag;
    /**
    * 是否删除(1删除/0不删除)
    */    private Integer enabledFlag;
    /**
    * 创建人
    */    private String createName;
    /**
    * 创建时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    /**
    * 更新人
    */    private String updateName;
    /**
    * 更新时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
    /**
     * 消息类型主键
     */
    private String msgTypeId;
    /**
     * 查询条件-开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private String startTime;
    /**
     * 查询条件-结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private String endTime;


    private String serviceModuleCode;
    private String serviceModuleName;

    /** 业务ID **/
    private String businessId;
    /** pc跳转地址*/
    String pcUrl;

    /**用户名 */
    String userName;
    /**用户ID */
    String userId;
    /** 未读数量*/
    String unread;
}