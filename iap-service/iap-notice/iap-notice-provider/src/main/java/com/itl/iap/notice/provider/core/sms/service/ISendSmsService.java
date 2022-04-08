package com.itl.iap.notice.provider.core.sms.service;


import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.notice.api.entity.MsgMailConfiguration;
import com.itl.iap.notice.api.entity.MsgPublicTemplate;
import com.itl.iap.notice.api.entity.email.model.SmsNotice;

/**
 * 短信服务标准接口
 *
 * @author 曾慧任
 * @date  2020/06/29
 * @since jdk1.8
 */
public interface ISendSmsService {
    /**
     * 发送短信
     * @param smsNotice 消息
     * @param msgPublicTemplate 消息模板
     * @param msgMailConfiguration 消息配置
     * @return SmsResult
     */
    ResponseData sendSms(SmsNotice smsNotice, MsgPublicTemplate msgPublicTemplate, MsgMailConfiguration msgMailConfiguration);
}
