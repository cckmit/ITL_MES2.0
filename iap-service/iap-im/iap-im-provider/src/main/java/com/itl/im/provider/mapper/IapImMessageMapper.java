/*
 * Copyright 2013-2019 Xia Jun(3979434@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ***************************************************************************************
 *                                                                                     *
 *                        Website : http://www.farsunset.com                           *
 *                                                                                     *
 ***************************************************************************************
 */
package com.itl.im.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import iap.im.api.dto.IapImMessageDto;
import iap.im.api.dto.IapImMessageListDto;
import iap.im.api.entity.IapImMessage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 消息Mapper
 *
 * @author tanq
 * @date 2020-10-10
 * @since jdk1.8
 */
public interface IapImMessageMapper extends BaseMapper<IapImMessage> {

    /**
     * 获取会话列表+会话
     *
     * @param user
     * @return
     */
    List<IapImMessageListDto> msgAll(@Param("user") String user);

    /**
     * 查询群组消息已读数和发消息人姓名及头像
     *
     * @return
     */
    List<IapImMessageDto> GroupSendMsg();

    /**
     * 消息历史记录
     *
     * @param page       分页
     * @param messageDto messageDto.sender 对方 messageDto.receiver 自己
     * @return
     */
    IPage<IapImMessageDto> messageHistory(Page<IapImMessageDto> page, @Param("messageDto") IapImMessageDto messageDto);

    /**
     * 文件消息历史
     *
     * @param page
     * @param iapImMessageDto
     * @return
     */
    IPage<IapImMessageListDto> fileMessages(Page<IapImMessageListDto> page, @Param("messageDto") IapImMessageDto iapImMessageDto, @Param("format") int[] format);

    /**
     * 修改消息状态
     *
     * @param senders 要修改的消息id集合
     */
    List<IapImMessageDto> findListMessage(@Param("senders") List<String> senders, @Param("receiver") String receiver, @Param("state") String state);

    /**
     * 修改消息状态
     *
     * @param list 要修改的消息id集合
     */
    void updateMsgState(List<IapImMessage> list);

    /**
     * 批量添加
     *
     * @param list
     * @return
     */
    boolean insertList(@Param("list") List<IapImMessage> list);

    IapImMessageListDto findOneMessageUser(@Param("sender") String sender, @Param("receiver") String receiver);
}
