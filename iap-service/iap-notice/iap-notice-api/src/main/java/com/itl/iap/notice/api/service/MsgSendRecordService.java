package com.itl.iap.notice.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.notice.api.dto.MsgSendRecordDto;
import com.itl.iap.notice.api.entity.MsgSendRecord;


import java.util.Map;

/**
 * 消息发送记录Service
 *
 * @author liaochengdian
 * @date 2020-03-25
 * @since jdk1.8
 */
public interface MsgSendRecordService extends IService<MsgSendRecord> {

    /**
     * 通过id查询
     *
     * @param id 消息发送记录id
     * @return MsgSendRecordDto
     */
    MsgSendRecordDto getById(String id);

    /**
     * 更改状态为已读
     *
     * @param msgSendRecord 消息发送实例
     */
    void updateReadFlag(MsgSendRecord msgSendRecord);

    IPage<MsgSendRecord> getMsgSendRecordList(MsgSendRecordDto query, Boolean isList);

    /**
     * 查询系统发送未读数量
     *
     * @param userId   接收人id
     * @param page     当前页
     * @param pageSize 页面大小
     * @return Map
     */
    Map getMsgSendRecordListByUser(String userId, Integer page, Integer pageSize);

    /**
     * 分页查询
     *
     * @param query 消息发送实例
     * @return IPage<MsgSendRecordDto>
     */
    IPage<MsgSendRecordDto> query(MsgSendRecordDto query);

    /**
     * 根据用户id获取发送的消息
     *
     * @param userId     用户id
     * @param noticeType 消息类型
     * @param page       当前页
     * @param pageNum    页面大小
     * @return IPage<Map < String, Object>>
     */
    IPage<Map<String, Object>> getMessageByUserName(String userId, Integer noticeType, Integer page, Integer pageNum);

    /**
     * 分页查询消息发送记录(首页)
     *
     * @param query 消息发送实例
     * @return IPage<MsgSendRecordDto>
     */
    IPage<MsgSendRecordDto> getReceiveListByUsername(MsgSendRecordDto query);

    /**
     * 查询接收人消息未读数
     *
     * @param username 接收人
     * @return Integer
     */
    Integer getUnread(String username);
}
