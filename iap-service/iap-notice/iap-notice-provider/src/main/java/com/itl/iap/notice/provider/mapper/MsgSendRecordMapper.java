package com.itl.iap.notice.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.itl.iap.notice.api.dto.MsgSendRecordDto;
import com.itl.iap.notice.api.entity.MsgSendRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 消息发送记录表(MsgSendRecord)表数据库访问层
 *
 * @author liaochengdian
 * @date 2020-03-25
 * @since jdk1.8
 */
public interface MsgSendRecordMapper extends BaseMapper<MsgSendRecord> {
    /**
     * 根据id查询详情
     *
     * @param id
     * @return
     */
    MsgSendRecordDto getById(@Param("id") String id,
                             @Param("status") String status,
                             @Param("serviceModule") String serviceModule,
                             @Param("sendType") String sendType);

    /**
     * 更新状态为已读
     *
     * @param msgSendRecord
     */
    void updateReadFlag(MsgSendRecord msgSendRecord);

    IPage<MsgSendRecord> getMsgSendRecordList(Page page, @Param("query") MsgSendRecordDto query);

    /**
     * 分页查询
     *
     * @param query 消息发送实例
     * @return IPage<MsgSendRecordDto>
     */
    IPage<MsgSendRecordDto> findList(Page page, @Param("query") MsgSendRecordDto query,
                                     @Param("status") String status,
                                     @Param("serviceModule") String serviceModule,
                                     @Param("sendType") String sendType);

    /**
     * 根据用户id获取发送的消息
     *
     * @param userId     用户id
     * @param noticeType 消息类型
     * @param page       分页
     * @return IPage<Map < String, Object>>
     */
    IPage<Map<String, Object>> findListByUsername(Page page, @Param("userId") String userId, @Param("noticeType") Integer noticeType);
//    IPage<MsgSendRecordDto> pageQuery(Page page, @Param("msgSendRecordDto") MsgSendRecordDto msgSendRecordDto);

    /**
     * 分页查询消息发送记录(首页)
     *
     * @param params 消息发送实例
     * @return IPage<MsgSendRecordDto>
     */
    IPage<MsgSendRecordDto> findReceiveListByUsername(Page page, @Param("query") MsgSendRecordDto params,
                                                      @Param("status") String status,
                                                      @Param("serviceModule") String serviceModule,
                                                      @Param("sendType") String sendType);

}