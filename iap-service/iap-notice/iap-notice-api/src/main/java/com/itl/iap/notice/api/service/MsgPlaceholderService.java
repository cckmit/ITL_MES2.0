package com.itl.iap.notice.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.notice.api.dto.MsgPlaceholderDto;
import com.itl.iap.notice.api.entity.MsgPlaceholder;
import com.itl.iap.notice.api.entity.MsgPlaceholderType;


import java.util.List;

/**
 * (MsgPlaceholder)表Service
 *
 * @author liaochengdian
 * @date  2020-04-07
 * @since jdk1.8
 */
public interface MsgPlaceholderService extends IService<MsgPlaceholder> {
    boolean checkOwn(MsgPlaceholder record);

    /**
     * 根据id获取占位符详情
     * @param id
     * @return MsgPlaceholder
     */
    MsgPlaceholder selectById(String id);

    /**
     * 构造占位符树,按类型分组
     * @param msgPlaceholderTypeId
     * @return List<MsgPlaceholderType>
     */
    List<MsgPlaceholderType> selectMsgPlaceholderTree(String msgPlaceholderTypeId);

    /**
     * 分页查询占位符
     * @param query 实例
     * @return IPage<MsgPlaceholder>
     */
    IPage<MsgPlaceholder> getMsgPlaceholderList(MsgPlaceholderDto query);
}
