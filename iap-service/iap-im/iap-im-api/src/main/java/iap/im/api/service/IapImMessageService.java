/*
 * Copyright ? 2017 海通安恒科技有限公司.
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
 *                        Website : http://www.htah.com.cn/                            *
 *                                                                                     *
 ***************************************************************************************
 */
package iap.im.api.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import iap.im.api.dto.IapImMessageDto;
import iap.im.api.dto.IapImMessageListDto;
import iap.im.api.dto.IapImMessageTopDto;
import iap.im.api.entity.IapImMessage;
import iap.im.api.entity.IapImMessageUser;

import java.util.List;
import java.util.Map;

/**
 * 消息Service层
 *
 * @author tanq
 * @date 2020-10-20
 * @since jdk1.8
 */
public interface IapImMessageService {

    /**
     * 个人消息发送方法
     *
     * @param iapImMessage
     * @return
     */
    IapImMessageDto sendMessage(IapImMessageDto iapImMessage);

    /**
     * 消息转发
     *
     * @param iapImMessageDto
     * @return
     */
    IapImMessageListDto forwardMessage(IapImMessageDto iapImMessageDto);

    /**
     * 保存消息内容
     *
     * @param iapImMessage
     * @return
     */
    boolean saveAllMessage(IapImMessage iapImMessage);

    /**
     * 撤回消息
     *
     * @param iapImMessageDto
     * @return
     * @author linjs
     */
    void revokeMessage(IapImMessageDto iapImMessageDto) throws Exception;

    /**
     * 查询消息+会话列表
     *
     * @return
     */
    List<IapImMessageListDto> msgAll(String user);

    /**
     * 查看消息历史
     *
     * @param messageDto messageDto.sender 对方 messageDto.receiver 自己
     * @return
     */
    IPage<IapImMessageDto> messageHistory(IapImMessageDto messageDto);

    /**
     * 敏感词过滤
     *
     * @param iapImMessageDto
     * @return
     */
    IapImMessageDto getDefaultFilter(IapImMessageDto iapImMessageDto);

    /**
     * 图片视频历史记录
     *
     * @param iapImMessageDto
     * @return
     */
    IPage<IapImMessageListDto> imgMessageHistory(IapImMessageDto iapImMessageDto);

    /**
     * 查看当前用户信息(发送卡片)
     *
     * @return
     */
    Map<String, String> findOnUser();

    /**
     * 批量已读
     *
     * @return
     */
    String updateListState(List<String> senders);

    /**
     * 阅读消息并回执
     *
     * @param otherSide 对方
     * @param mark      标记，用于区分用户消息还是群消息
     */
    String updateMsgState(String otherSide, short mark);

    /**
     * 消息内容置顶
     *
     * @param messageTopDto 置顶消息
     */
    void addMessageTop(IapImMessageTopDto messageTopDto);

    /**
     * 消息内容取消置顶
     *
     * @param id 置顶id
     */
    void deleteMessageTop(String id);

    /**
     * 消息内容删除
     *
     * @param messageId 消息ID
     */
    boolean deleteMessages(List<String> messageId);

    /**
     * 发起聊天
     *
     * @param iapImMessage
     * @return
     */
    IapImMessageUser saveOrUpdateUser(IapImMessage iapImMessage);
}
