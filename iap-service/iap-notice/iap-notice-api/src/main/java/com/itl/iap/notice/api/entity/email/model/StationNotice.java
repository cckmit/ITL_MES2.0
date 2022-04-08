package com.itl.iap.notice.api.entity.email.model;

import lombok.Data;

/**
 * 站内通知
 *
 * @author liaochengdian
 * @date  2020/3/19
 * @since jdk1.8
 */
@Data
public class StationNotice extends BaseNotice{
    private int type;
    /**
     * 主题
     */
    private String title;
    /**
     * 内容
     */
    private String content;

    /**
     * 接收人Id
     */
    private String userId;
    /**
     * 接收人姓名
     */
    private String userName;

}
