package com.itl.iap.notice.provider.core.email.server;


import com.itl.iap.notice.provider.core.email.config.EmailConfig;
import com.itl.iap.notice.provider.core.email.vo.MailData;

/**
 * 邮件发送接口
 *
 * @author 曾慧任
 * @date  2020/06/29
 * @since jdk1.8
 */
public interface ISendEmailService {
    /**
     * 发送简单的文本邮件
     * @throws Exception
     */
    void sendMail(MailData mailData, EmailConfig emailConfig) throws Exception;

    /**
     * 发送有附件的邮件
     * @throws Exception
     */
    void sendMailWithAttachFile(MailData mailData) throws Exception;

    /**
     * 发送HTML内容的邮件
     * @throws Exception
     */
    void sendHtmlMail(MailData mailData)throws Exception;

}
