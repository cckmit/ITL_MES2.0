package com.itl.iap.notice.provider.core.email.server.impl;


import com.itl.iap.notice.provider.core.email.config.EmailConfig;
import com.itl.iap.notice.provider.core.email.server.ISendEmailService;
import com.itl.iap.notice.provider.core.email.util.SendEmailUtil;
import com.itl.iap.notice.provider.core.email.vo.MailData;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;

/**
 * 邮件发送实现类
 *
 * @author 曾慧任
 * @date 2020/06/29
 * @since jdk1.8
 */
@Service("sendEmailService")
public class SendEmailServiceImpl implements ISendEmailService {

    /**
     * 发送简单的文本邮件
     *
     * @throws Exception
     */
    @Override
    public void sendMail(MailData mailData, EmailConfig emailConfig) throws Exception {
        Session session = SendEmailUtil.initProperties(emailConfig.getUsername(), emailConfig.getPassword(), emailConfig.getProtocol(), emailConfig.getMailServerHost(), emailConfig.getMailServerPort(), isSendSsl(emailConfig.getProtocol()));
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(new InternetAddress(emailConfig.getUsername()));// 发件人,可以设置发件人的别名

        Address[] toInternetAddress = mailData.getToInternetAddress();
        if (toInternetAddress == null) {
            throw new RuntimeException("收件人不能为空");
        }
        //邮件接收人
        mimeMessage.addRecipients(Message.RecipientType.TO, toInternetAddress);
        if (mailData.getCcInternetAddress() != null) {
            //抄送地址
            mimeMessage.addRecipients(Message.RecipientType.CC, mailData.getCcInternetAddress());
        }
        mimeMessage.setSubject(mailData.getSubject());
        mimeMessage.setSentDate(new Date());
        Multipart mainParth = new MimeMultipart();
        BodyPart html = new MimeBodyPart();
        html.setContent(mailData.getContent(), "text/html; charset=UTF-8");
        mainParth.addBodyPart(html);
//       MimeBodyPart bodyPart = new MimeBodyPart();
//        if (fileSrc != null && fileSrc.length > 0) {
//            URL url;
//            for (int i = 0; i < fileSrc.length; i++) {
//                String devAgreement = "http";
//                if (fileSrc[i].startsWith(devAgreement)) {
//                    bodyPart = new MimeBodyPart();
//                    url = new URL(fileSrc[i]);
//                    DataSource dataSource = new URLDataSource(url);
//                    DataHandler dataHandler = new DataHandler(dataSource);
//                    bodyPart.setDataHandler(dataHandler);
//                    mainParth.addBodyPart(bodyPart);
//                }
//            }
//        }
        mimeMessage.setContent(mainParth);
        Transport.send(mimeMessage);

        //  SendEmailUtil.send(session, mailData.getSubject(),mailData.getContent(),mailData.getToInternetAddress(),null,emailConfig.getUsername(),emailConfig.getPassword(),null,mailData.getCcInternetAddress());

    }

    /**
     * 发送有附件的邮件
     *
     * @throws Exception
     */
    @Override
    public void sendMailWithAttachFile(MailData mailData) throws Exception {
    }

    /**
     * 发送HTML内容的邮件
     *
     * @throws Exception
     */
    @Override
    public void sendHtmlMail(MailData mailData) throws Exception {
    }

    public boolean isSendSsl(String protocol) throws Exception {
        if ("imap".equals(protocol)) {
            return true;
        }
        return false;
    }
}
