package com.itl.iap.notice.provider.core.email.vo;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.*;

/**
 * 发送邮箱vo对象
 *
 * @author 曾慧任
 * @date  2020/06/29
 * @since jdk1.8
 */
@Data
public class MailData {

    /**
     * 接收邮件的邮箱,用逗号分隔标识多个
     */
    private String toAddresss;
    /**
     * 抄送地址
     */
    private String ccAddresss;
    /**
     * 邮件主题
     */
    private String subject;
    /**
     * 邮件的文本内容
     */
    private String content;
    /**
     * 邮件附件的文件名
     */
    private Vector<String> attachFileNames = new Vector<>();;
    private String content_type = "text/html; charset=utf-8";
    /**
     * 邮件模板的文件名
     */
    private String fileName;
    /**
     * 模板的参数
     */
    private Map<String,Object> params;
    /**
     * 获得收件人地址
     * @return Address[]
     */
    public Address[] getToInternetAddress(){
        if(StringUtils.isNotBlank(this.toAddresss)){
            List<String> toAddresss = Arrays.asList(this.toAddresss.split(","));
            Address[] addresses = new Address[]{};
            List<InternetAddress> internetAddressList = new ArrayList<>();
            toAddresss.forEach(toAddress->{
                try {
                    internetAddressList.add(new InternetAddress(toAddress));
                } catch (AddressException e) {
                    e.printStackTrace();
                }
            });
            return (Address[]) internetAddressList.toArray(addresses);
        }
        return null;
    }

    /**
     * 获得抄送地址
     * @return Address[]
     */
    public Address[] getCcInternetAddress(){
        if(StringUtils.isNotBlank(this.ccAddresss)){
            List<String> ccAddresss = Arrays.asList(this.ccAddresss.split(","));
            Address[] addresses = new Address[]{};
            List<InternetAddress> internetAddressList = new ArrayList<>();
            ccAddresss.forEach(ccAddress->{
                try {
                    internetAddressList.add(new InternetAddress(ccAddress));
                } catch (AddressException e) {
                    e.printStackTrace();
                }
            });
            return (Address[]) internetAddressList.toArray(addresses);
        }
        return null;
    }
}
