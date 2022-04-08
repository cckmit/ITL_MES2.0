package com.itl.iap.notice.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.iap.common.base.model.BaseModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 消息发送记录表(MsgSendRecord)实体类
 *
 * @author liaochengdian
 * @date  2020-03-25
 * @since jdk1.8
 */
@Data
@Accessors(chain = true)
@TableName("msg_send_record")
public class MsgSendRecord extends BaseModel {
    private static final long serialVersionUID = -61647577699421157L;

    private String id;
    /**
    * 规则类型
    */
    @TableField("send_type")
    private Integer sendType;
    /**
    * 发送状态(1.发送中/2.发送成功/3.发送失败)
    */ private Integer status;
    /**
    * 发送主题
    */private String title;
    /**
    * 发送内容
    */private String content;
    /**
    * 公共消息模板Id
    */
    @TableField("msg_public_template_id")
    private String msgPublicTemplateId;
    /**
    * 通知类型
    */
    @TableField("notice_type_code")
    private Integer noticeTypeCode;
    /**
    * 消息类型
    */
    @TableField("msg_type")
    private String msgType;
    /**
    * 接收人主键
    */
    @TableField("receiver_uid")
    private String receiverUid;
    /**
    * 接收人姓名
    */
    @TableField("receiver_name")
    private String receiverName;
    /**
    * 发送时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale = "zh", timezone = "GMT+8")
    @TableField("send_time")
    private Date sendTime;
    /**
    * 已读状态(1.已读/0.未读)
    */
    @TableField("read_flag")
    private Integer readFlag=0;
    /**
    * 删除标识(1删除/0不删除)
    */
    @TableField("enabled_flag")
    private Integer enabledFlag=0;
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
    /** 业务ID **/
    @TableField("business_id")
    private String businessId;

    /** 名称字段*/
    @TableField(exist = false)
    private String sendTypeName;
    @TableField(exist = false)
    private String noticeTypeName;
    @TableField(exist = false)
    private String statusName;
}