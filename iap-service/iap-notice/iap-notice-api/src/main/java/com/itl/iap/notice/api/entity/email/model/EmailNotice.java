package com.itl.iap.notice.api.entity.email.model;

import lombok.Data;

/**
 * 邮件通知内容类
 *
 * @author liaochengdian
 * @date  2020/3/19
 * @since jdk1.8
 */
@Data
public class EmailNotice extends BaseNotice {

    private String toAddress;//发送地址   不发邮件不用传  以，分割

    private String ccAddress;//抄送地址   不发邮件不用传  以，分割

    /**接收人Id */
    private String userId;
    /**接收人姓名 */
    private String userName;
}
