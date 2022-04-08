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
package com.itl.im.provider.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.util.DtoUtils;
import com.itl.iap.system.api.dto.IapSysUserTDto;
import com.itl.im.provider.core.push.MessageListTemplatePusher;
import com.itl.im.provider.core.push.provider.MessageTemplate;
import com.itl.im.provider.core.sensitive.SensitiveFilter;
import com.itl.im.provider.mapper.*;
import com.itl.im.provider.util.MessageUtil;
import com.itl.im.provider.util.SnowIdUtil;
import iap.im.api.dto.*;
import iap.im.api.entity.*;
import iap.im.api.service.IapImMessageService;
import iap.im.api.variable.CIMConstant;
import iap.im.api.variable.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 消息Service层实现类
 *
 * @author tanq
 * @date 2020-10-20
 * @since jdk1.8
 */
@Service
@Slf4j
public class IapImMessageServiceImpl extends ServiceImpl<IapImMessageMapper, IapImMessage> implements IapImMessageService {

    private static final long TWO_MINUTES = 2 * 1000 * 60;

    private static final char REPLACE_WORD = '*';
    @Autowired
    private UserUtil userUtil;

    @Resource
    private IapImMessageMapper messageMapper;

    @Resource
    private IapImGroupMemberMapper iapImGroupMemberMapper;

    @Resource
    private IapImSensitiveWordsMapper iapImSensitiveWordsMapper;

    @Resource
    private IapImMessageTopMapper messageTopMapper;

    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private MessageListTemplatePusher messageListTemplatePusher;
    @Resource
    private IapImMessageUserMapper iapImMessageUserMapper;
    @Resource
    private IapImMessageSettingMapper iapImMessageSettingMapper;

    /**
     * 查询消息+会话列表
     *
     * @return
     */
    @Override
    public List<IapImMessageListDto> msgAll(String user) {
        if (StringUtils.isEmpty(user)) {
            user = userUtil.getUser().getUserName();
        }
        // 会话列表
        List<IapImMessageListDto> messageList = messageMapper.msgAll(user);
        // 群消息为1的消息
        List<IapImMessageDto> iapImMessages = messageMapper.GroupSendMsg();
        // 群成员
        List<IapImGroupMember> allGroupMeaber = iapImGroupMemberMapper.selectList(new QueryWrapper<>());
        messageList.parallelStream().forEach(msgList -> {
            // 设置会话列表最新时间
            Long newTim = msgList.getIapImMessageDtos().stream().map(IapImMessageDto::getTimestamp).max(Long::compareTo).get();
            msgList.setTimestamp(newTim);
            msgList.getIapImMessageDtos().sort(Comparator.comparing(IapImMessageDto::getTimestamp));
            if (msgList.getListType() == 1) {
                msgList.getIapImMessageDtos().forEach(msg -> {
                    this.groupMsgHandle(iapImMessages, msg);
                });
                List<IapImGroupMember> allMeaber = allGroupMeaber.stream()
                        .filter(gourpId -> gourpId.getGroupId().equals(msgList.getReceiver()))
                        .collect(Collectors.toList());
                msgList.setGroupMemberList(allMeaber);
            }
        });
        // 消息列表置顶
        List<IapImMessageListDto> topList = messageList.stream().filter(msg -> Short.toString(Constants.MessageUserDto.SHOW_TYPE2).equals(Short.toString(msg.getShowType()))).collect(Collectors.toList());
        if (!topList.isEmpty()) {
            topList.sort(Comparator.comparing(IapImMessageListDto::getTopTime).reversed());
            List<IapImMessageListDto> otherList = messageList.stream().filter(msg -> !Short.toString(Constants.MessageUserDto.SHOW_TYPE2).equals(Short.toString(msg.getShowType())))
                    .sorted(Comparator.comparing(IapImMessageListDto::getTimestamp).reversed())
                    .collect(Collectors.toList());
            topList.addAll(otherList);
            return topList;
        }
        // 加载默认敏感词汇词典
        SensitiveFilter filter = getDefaultSensitiveFilter();
        //对敏感词汇进行过滤
        messageList.parallelStream().forEach(msgList -> {
            msgList.getIapImMessageDtos().removeIf(x -> StringUtils.isEmpty(x.getId()));
            msgList.getIapImMessageDtos().stream().forEach(imd -> imd.setContent(filter.filter(imd.getContent(), REPLACE_WORD)));
        });
        messageList.sort(Comparator.comparing(IapImMessageListDto::getTimestamp).reversed());
        messageList.sort(Comparator.comparing(IapImMessageListDto::getMessageNumber).reversed());
        messageList.sort(Comparator.comparing(IapImMessageListDto::getShowType).reversed());
        messageList.sort(Comparator.comparing(IapImMessageListDto::getTopTime));
        return messageList;
    }

    /**
     * 消息历史
     *
     * @param messageDto messageDto.sender 对方 messageDto.receiver 自己
     * @return
     */
    @Override
    public IPage<IapImMessageDto> messageHistory(IapImMessageDto messageDto) {
        messageDto.setSender(userUtil.getUser().getUserName());
        // 加载默认敏感词汇词典
        SensitiveFilter filter = getDefaultSensitiveFilter();
        if (null == messageDto.getPage()) {
            messageDto.setPage(new Page(0, 20));
        }

        IPage<IapImMessageDto> messageList = messageMapper.messageHistory(messageDto.getPage(), messageDto);
        List<IapImMessageDto> iapImMessageDtos = messageMapper.GroupSendMsg();
        messageList.getRecords().stream().forEach(msg -> {
            this.groupMsgHandle(iapImMessageDtos, msg);
            //对敏感词汇进行过滤
            msg.setContent(filter.filter(msg.getContent(), REPLACE_WORD));
        });
        return messageList;
    }

    /**
     * 消息内容过滤
     *
     * @param iapImMessageDto
     * @return
     */
    @Override
    public IapImMessageDto getDefaultFilter(IapImMessageDto iapImMessageDto) {
        SensitiveFilter filter = getDefaultSensitiveFilter();
        if (iapImMessageDto != null && StringUtils.isNotEmpty(iapImMessageDto.getContent())) {
            iapImMessageDto.setContent(filter.filter(iapImMessageDto.getContent(), REPLACE_WORD));
        }
        return iapImMessageDto;
    }

    /**
     * 图片视频历史记录
     *
     * @param iapImMessageDto
     * @return
     */
    @Override
    public IPage<IapImMessageListDto> imgMessageHistory(IapImMessageDto iapImMessageDto) {
        iapImMessageDto.setSender(userUtil.getUser().getUserName());
        if (null == iapImMessageDto.getPage()) {
            iapImMessageDto.setPage(new Page(0, 20));
        }
        int[] ints = {1, 5};
        return messageMapper.fileMessages(iapImMessageDto.getPage(), iapImMessageDto, ints);
    }

    /**
     * 处理群组转发消息
     *
     * @param messages 群转发消息的主消息
     * @param msg      群转发消息
     */
    private void groupMsgHandle(List<IapImMessageDto> messages, IapImMessageDto msg) {
        messages.stream().filter(groupSendMsg -> Long.toString(msg.getTimestamp()).equals(Long.toString(groupSendMsg.getTimestamp()))).forEach(groupSendMsg -> {
            // 设置群组发消息人的名称和头像
            if (Constants.Message.ACTION_3.equals(msg.getAction())) {
                msg.setSenderRealName(groupSendMsg.getSenderRealName());
                msg.setSender(groupSendMsg.getReceiver());
                msg.setSendAvatar(groupSendMsg.getSendAvatar());
            }
            // 设置群消息已读数量
            msg.setMessageNumber(groupSendMsg.getMessageNumber());
            // 设置群员数
            msg.setGroupNumber(groupSendMsg.getGroupNumber());
        });
    }

    /**
     * 个人消息发送方法
     *
     * @param iapImMessageDto
     * @return
     */
    @Override
    public IapImMessageDto sendMessage(IapImMessageDto iapImMessageDto) {
        // 更新消息列表
        if (!this.onMessageUser(DtoUtils.convertObj(iapImMessageDto, IapImMessage.class))) {
            this.updateMsgState(iapImMessageDto.getReceiver(), Constants.MessageUserDto.LIST_TYPE0);
            // 发送消息 返回false 则代表用户不在线，触发消息自动回复
            if (!this.sendSenderList(iapImMessageDto.getReceiver())) {
                this.userNotOnline(iapImMessageDto);
            }
        }
        return iapImMessageDto;
    }

    /**
     * 消息转发
     *
     * @param iapImMessageDto
     * @return
     */
    @Override
    public IapImMessageListDto forwardMessage(IapImMessageDto iapImMessageDto) {
        if (!this.onMessageUser(DtoUtils.convertObj(iapImMessageDto, IapImMessage.class))) {
            this.updateMsgState(iapImMessageDto.getReceiver(), Constants.MessageUserDto.LIST_TYPE0);
        }
//        this.sendSenderList(iapImMessageDto.getSender());
        if (!this.sendSenderList(iapImMessageDto.getReceiver())) {
            this.userNotOnline(iapImMessageDto);
        }
        IapImMessageListDto oneMessageUser = messageMapper.findOneMessageUser(iapImMessageDto.getSender(), iapImMessageDto.getReceiver());
        oneMessageUser.setTimestamp(new Date().getTime());
        oneMessageUser.getIapImMessageDtos().stream().sorted(Comparator.comparingLong(IapImMessageDto::getTimestamp).reversed()).collect(Collectors.toList());
        return oneMessageUser;
    }

    /**
     * 保存消息内容
     *
     * @param iapImMessage
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveAllMessage(IapImMessage iapImMessage) {
        return this.save(iapImMessage);
    }

    /**
     * 撤回消息
     *
     * @param iapImMessageDto
     * @return ResponseData
     * @author linjs
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void revokeMessage(IapImMessageDto iapImMessageDto) throws Exception {
        try {
            String id = iapImMessageDto.getId();
            Assert.hasText(id, "ID数据缺失");
            //获取源消息
            IapImMessage iapImMessage = messageMapper.selectById(id);
            iapImMessage.setContent("消息已撤回");
            iapImMessage.setFormat(Constants.MessageAction.ACTION_101);
            //只允许两分钟内撤回
            Date now = new Date();
            if ((now.getTime() - iapImMessage.getTimestamp()) > TWO_MINUTES) {
                throw new Exception("只允许撤回两分钟内消息");
            }
            //撤回消息以删除源消息处理
            int result = messageMapper.updateById(iapImMessage);
            if (result == 1) {
                this.sendSenderList(iapImMessage.getReceiver());
            } else {
                throw new Exception("撤回失败");
            }
        } catch (Exception e) {
            log.error(e.toString());
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 像指定人发送消息
     *
     * @param user 用户
     */
    private boolean sendSenderList(String user) {
        return messageListTemplatePusher.sendMessage(this.msgAll(user), user);
    }

    /**
     * 发送个人名片
     *
     * @return
     */
    @Override
    public Map<String, String> findOnUser() {
        IapSysUserTDto user = JSONObject.parseObject(redisTemplate.opsForValue().get(userUtil.getUser().getUserName() + "-user").toString(), IapSysUserTDto.class);
        Map<String, String> map = new ManagedMap<>();
        map.put("userName", user.getUserName());
        map.put("realName", user.getRealName());
        map.put("nickName", user.getNickName());
        map.put("userMobile", user.getUserMobile());
        map.put("email", user.getEmail());
        map.put("format", Constants.MessageFormat.FORMAT_CARD);
        return map;
    }

    /**
     * 消息全部已读
     */
    @Override
    public String updateListState(List<String> senders) {
        String self = userUtil.getUser().getUserName();
        List<IapImMessageDto> list = messageMapper.findListMessage(senders, self, Constants.Message.STATE_READ);
        messageMapper.updateMsgState(DtoUtils.convertList(IapImMessage.class, list));
        list.forEach(x -> {
            if (!x.getReceiver().equals(self)) {
                this.sendSenderList(x.getReceiver());
            }
            if (!x.getSender().equals(self)) {
                this.sendSenderList(x.getSender());
            }
        });
        return Constants.MessageAction.ACTION_108;
    }

    /**
     * 消息已读
     *
     * @param otherSide 对方
     * @param mark      标记，用于区分用户消息还是群消息
     * @return
     */
    @Override
    public String updateMsgState(String otherSide, short mark) {
        String self = userUtil.getUser().getUserName();
        QueryWrapper<IapImMessage> query = new QueryWrapper<IapImMessage>()
                .eq("sender", otherSide)
                .eq("receiver", self)
                .ne("state", Constants.Message.STATE_READ)
                .select("id");
        List<IapImMessage> list = this.list(query);
        if (!list.isEmpty()) {
            messageMapper.updateMsgState(list);
            MessageTemplate messageTemplate = new MessageTemplate(CIMConstant.ProtobufType.MESSAGE_REDA);
            IapImMessageListDto iapImMessageDto = new IapImMessageListDto();
            if (Constants.MessageUserDto.LIST_TYPE1 == mark) {
                iapImMessageDto.setListType(Constants.MessageUserDto.LIST_TYPE1);
                List<String> accountList = iapImGroupMemberMapper.selectGroupMemberListByGroupId(otherSide).stream().map(IapImGroupMember::getAccount).collect(Collectors.toList());
                accountList.forEach(account -> {
                    iapImMessageDto.setReceiver(account);
                    iapImMessageDto.setSender(self);
                    messageTemplate.putAll(iapImMessageDto);
                    messageListTemplatePusher.setAllMessages(messageTemplate, account);
                });
            } else {
                iapImMessageDto.setListType(Constants.MessageUserDto.LIST_TYPE0);
                iapImMessageDto.setReceiver(otherSide);
                iapImMessageDto.setSender(self);
                messageTemplate.putAll(iapImMessageDto);
                messageListTemplatePusher.setAllMessages(messageTemplate, otherSide);
            }
            return Constants.MessageAction.ACTION_108;
        }
        return null;
    }

    @Override
    public void addMessageTop(IapImMessageTopDto messageTopDto) {
        IapImMessageTop messageTop = DtoUtils.convertObj(messageTopDto, IapImMessageTop.class);
        messageTop.setId(SnowIdUtil.getLongId());
        messageTopMapper.insert(messageTop);
    }

    /**
     * 删除消息置顶
     *
     * @param id 置顶id
     */
    @Override
    public void deleteMessageTop(String id) {
        messageTopMapper.deleteById(id);
    }

    /**
     * 修改消息列表 并推送消息
     *
     * @param iapImMessage
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public IapImMessageUser saveOrUpdateUser(IapImMessage iapImMessage) {
        IapImMessageUser iapImMessageUser = iapImMessageUserMapper.selectOne(new QueryWrapper<IapImMessageUser>().eq("sender", iapImMessage.getSender()).eq("receiver", iapImMessage.getReceiver()));
        IapImMessageUser iapImReUser = iapImMessageUserMapper.selectOne(new QueryWrapper<IapImMessageUser>().eq("sender", iapImMessage.getReceiver()).eq("receiver", iapImMessage.getSender()));
        if (iapImMessageUser == null) {
            IapImMessageUser iapImUser = new IapImMessageUser();
            iapImUser.setId(SnowIdUtil.getLongId());
            iapImUser.setSender(iapImMessage.getSender());
            iapImUser.setReceiver(iapImMessage.getReceiver());
            iapImUser.setListType(Constants.MessageUserDto.LIST_TYPE0);
            iapImUser.setShowType(Constants.MessageUserDto.SHOW_TYPE0);
            iapImMessageUserMapper.insert(iapImUser);
            this.updateMsgState(iapImMessage.getReceiver(), Constants.MessageUserDto.LIST_TYPE0);
//            this.sendSenderList(iapImMessage.getSender());
//            this.sendSenderList(iapImMessage.getReceiver());
            return iapImUser;
        } else {
            if (iapImReUser == null) {
                IapImMessageUser iapImUser = new IapImMessageUser();
                iapImUser.setId(SnowIdUtil.getLongId());
                iapImUser.setSender(iapImMessage.getReceiver());
                iapImUser.setReceiver(iapImMessage.getSender());
                iapImUser.setListType(Constants.MessageUserDto.LIST_TYPE0);
                iapImUser.setShowType(Constants.MessageUserDto.SHOW_TYPE0);
                iapImMessageUserMapper.insert(iapImUser);
                return iapImUser;
            }
            if (Constants.MessageUserDto.SHOW_TYPE1 == iapImMessageUser.getShowType()) {
                iapImMessageUser.setShowType(Constants.MessageUserDto.SHOW_TYPE0);
                iapImMessageUserMapper.updateById(iapImMessageUser);
                this.updateMsgState(iapImMessage.getReceiver(), Constants.MessageUserDto.LIST_TYPE0);
//                this.sendSenderList(iapImMessage.getSender());
            }
        }
        return iapImMessageUser;
    }

    /**
     * 消息发送判断用户是否存在
     *
     * @param iapImMessage
     * @return
     */
    private boolean onMessageUser(IapImMessage iapImMessage) {
        IapImMessageUser iapImMessageUser = iapImMessageUserMapper.selectOne(new QueryWrapper<IapImMessageUser>().eq("sender", iapImMessage.getReceiver()).eq("receiver", iapImMessage.getSender()));
        if (iapImMessageUser == null) {
            IapImMessageUser iapImUser = new IapImMessageUser();
            iapImUser.setId(SnowIdUtil.getLongId());
            iapImUser.setSender(iapImMessage.getReceiver());
            iapImUser.setReceiver(iapImMessage.getSender());
            iapImUser.setListType(Constants.MessageUserDto.LIST_TYPE0);
            iapImUser.setShowType(Constants.MessageUserDto.SHOW_TYPE0);
            iapImMessageUserMapper.insert(iapImUser);
            return false;
        } else {
            if (Constants.MessageUserDto.SHOW_TYPE1 == iapImMessageUser.getShowType()) {
                iapImMessageUser.setShowType(Constants.MessageUserDto.SHOW_TYPE1);
                iapImMessageUserMapper.updateById(iapImMessageUser);
                this.updateMsgState(iapImMessage.getReceiver(), Constants.MessageUserDto.LIST_TYPE0);
                // 发送消息 返回false 则代表用户不在线，触发消息自动回复
                if (!this.sendSenderList(iapImMessage.getSender())) {
                    this.userNotOnline(DtoUtils.convertObj(iapImMessage, IapImMessageDto.class));
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 用户不在线发送自动回复参数
     */
    private void userNotOnline(IapImMessageDto iapImMessageDto) {
        IapImMessage iapImMessage = DtoUtils.convertObj(iapImMessageDto, IapImMessage.class);
        IapImMessageSettingDto iapImMessageSetting = new IapImMessageSettingDto();
        iapImMessageSetting.setUserId(iapImMessageDto.getReceiver());
        IapImMessageSettingDto iapImMessageSettingDto = iapImMessageSettingMapper.queryUserSetting(iapImMessageSetting);
        // 是否开启自动回复
        if (iapImMessageSettingDto == null || CollectionUtils.isEmpty(iapImMessageSettingDto.getCustomizeDto()) || iapImMessageSettingDto.getAutoType().equals(Constants.MessageSetting.AUTO_TYPE0)) {
            return;
        }
        // 获取历史自动回复消息
        List<IapImMessage> newMessages = messageMapper.selectList(new QueryWrapper<IapImMessage>().eq("sender", iapImMessageDto.getReceiver()).eq("receiver", iapImMessage.getSender()).eq("format", Constants.MessageAction.ACTION_118).orderByDesc("timestamp"));
        // 自动时间频率判断，如果返回为true 则不发送消息
        if (CollectionUtils.isNotEmpty(newMessages) && MessageUtil.autoReplyTime(iapImMessageSettingDto, newMessages)) {
            return;
        }
        iapImMessage.setId(SnowIdUtil.getLongId());
        iapImMessage.setAction(Constants.Message.ACTION_0);
        iapImMessage.setState(Constants.Message.STATE_NOT_RECEIVED);
        iapImMessage.setFormat(Constants.MessageAction.ACTION_118);
        iapImMessage.setReceiver(iapImMessageDto.getSender());
        iapImMessage.setSender(iapImMessageDto.getReceiver());
        iapImMessage.setTimestamp(new Date().getTime());
        iapImMessage.setContent(iapImMessageSettingDto.getCustomizeDto().get(0).getCustomizeMessage());
        this.save(iapImMessage);
        this.sendSenderList(iapImMessageDto.getSender());
    }

    /**
     * 消息删除
     *
     * @param messageId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteMessages(List<String> messageId) {
        String userName = userUtil.getUser().getUserName();
        List<IapImMessage> iapImMessages = messageMapper.selectBatchIds(messageId);
        if (iapImMessages.isEmpty()) {
            return false;
        }
        List<IapImMessage> notMessage = iapImMessages.stream().filter(hide -> StringUtils.isEmpty(hide.getHideMessage())).collect(Collectors.toList());
        List<String> allId = iapImMessages.stream().filter(hide -> StringUtils.isNotEmpty(hide.getHideMessage())).map(ids -> ids.getId()).collect(Collectors.toList());
        for (IapImMessage hid : notMessage) {
            hid.setHideMessage(userName);
        }
        if (!notMessage.isEmpty()) {
            this.updateBatchById(notMessage);
        }
        if (!allId.isEmpty()) {
            this.removeByIds(allId);
        }
        return true;
    }

    private SensitiveFilter getDefaultSensitiveFilter() {
        SensitiveFilter filter = new SensitiveFilter();
        try {
            //获取所有敏感词汇
            List<IapImSensitiveWords> sensitiveWordsList = iapImSensitiveWordsMapper.getAllSensitiveWords();
            if (CollectionUtils.isNotEmpty(sensitiveWordsList)) {
                sensitiveWordsList.stream().forEach(sw -> filter.put(sw.getWord()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filter;
    }


}

