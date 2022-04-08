package com.itl.iap.notice.api.service;


import java.util.Map;

/**
 * 发送通知服务类
 *
 * @author 曾慧任
 * @date  2020-06-29
 * @since jdk1.8
 */
public interface NoticeService {
    /**
     *  发送消息
     * @param notice
     */
    void sendMessage(Map<String, Object> notice);

    /**
     * 更改状态为已读
     *
     * @param id
     */
    void updateReadFlag(String id);

    Map getMsgSendRecordListByUser(String userId, Integer page , Integer pageSize);
}
