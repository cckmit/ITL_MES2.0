package com.itl.iap.notice.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.notice.api.dto.MsgTypeDto;
import com.itl.iap.notice.api.entity.MsgType;


import java.util.List;

/**
 * 消息类型Service
 *
 * @author liaochengdian
 * @date  2020-03-25
 * @since jdk1.8
 */
public interface MsgTypeService extends IService<MsgType> {

    /**
     * 查询消息类型树
     * @param id 消息类型id
     * @return List<MsgType>
     */
    List<MsgType> selectMsgTypeTree(String id);

    /**
     * 获取消息类型详情
     * @param id 消息类型id
     * @return MsgType
     */
    MsgType selectById(String id);

    List<MsgType> selectDynamicMsgTypes(MsgTypeDto msgTypeDto);

    boolean checkOwn(MsgType record);
}
