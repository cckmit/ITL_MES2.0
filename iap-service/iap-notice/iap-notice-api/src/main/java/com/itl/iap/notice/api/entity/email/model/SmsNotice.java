package com.itl.iap.notice.api.entity.email.model;

import lombok.Data;

/**
 * 短信通知内容类
 *
 * @author liaochengdian
 * @date  2020/3/19
 * @since jdk1.8
 */
@Data
public class SmsNotice extends BaseNotice{
    /**
     * 电话列表
     */
    private String phones;

}
