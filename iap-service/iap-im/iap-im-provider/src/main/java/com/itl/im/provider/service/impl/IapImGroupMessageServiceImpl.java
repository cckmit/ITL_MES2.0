package com.itl.im.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.common.base.dto.UserTDto;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.util.DtoUtils;
import com.itl.im.provider.core.push.MessageListTemplatePusher;
import com.itl.im.provider.mapper.IapImGroupMapper;
import com.itl.im.provider.mapper.IapImGroupMemberMapper;
import com.itl.im.provider.mapper.IapImMessageMapper;
import com.itl.im.provider.mapper.IapImMessageUserMapper;
import com.itl.im.provider.util.MessageUtil;
import com.itl.im.provider.util.SnowIdUtil;
import com.itl.im.provider.util.ThreadPoolExpansion;
import iap.im.api.dto.IapImGroupMemberDto;
import iap.im.api.dto.IapImMessageDto;
import iap.im.api.dto.IapImMessageListDto;
import iap.im.api.entity.IapImGroup;
import iap.im.api.entity.IapImGroupMember;
import iap.im.api.entity.IapImMessage;
import iap.im.api.entity.IapImMessageUser;
import iap.im.api.service.IapImGroupMessageService;
import iap.im.api.service.IapImMessageService;
import iap.im.api.variable.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 群消息Service层
 *
 * @author tanq
 * @date 2020-10-20
 * @since jdk1.8
 */
@Service
public class IapImGroupMessageServiceImpl implements IapImGroupMessageService {

    @Resource
    private MessageListTemplatePusher messageListTemplatePusher;
    @Resource
    private ThreadPoolExpansion threadPoolExpansion;
    @Resource
    private IapImGroupMemberMapper iapImGroupMemberMapper;
    @Resource
    private IapImMessageMapper iapImMessageMapper;
    @Resource
    private IapImGroupMapper iapImGroupMapper;
    @Resource
    private IapImMessageUserMapper iapImMessageUserMapper;
    @Resource
    private IapImMessageService iapImMessageService;
    @Resource
    private UserUtil userUtil;
    @Value("#{'${server.host}'.trim()}")
    private String host;

    /**
     * 群消息发送
     *
     * @param iapImMessageDto 当前消息
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public IapImMessageDto sendMessage(IapImMessageDto iapImMessageDto) {
        String userName = userUtil.getUser().getUserName();
        iapImMessageDto.setSender(userName);
        iapImMessageDto.setId(SnowIdUtil.getLongId());
        iapImMessageDto.setTimestamp(System.currentTimeMillis());
        // 判断该发送者的发送权限
        if (this.onSendMessage(iapImMessageDto)) {
            List<IapImMessage> iapImMessages = this.saveGroupMessages(iapImMessageDto);
            // 消息保存到数据库
            List<String> userList = iapImMessages.parallelStream().map(IapImMessage::getReceiver).collect(Collectors.toList());
            userList.removeIf(user -> user.equals(iapImMessageDto.getReceiver()) || user.equals(iapImMessageDto.getSender()));  // 删除接收者为和为自己的用户
            iapImMessageMapper.insertList(iapImMessages);
            // 发消息给群成员
            this.sendGroupMessage(userList);
        }
        // 过滤敏感词
        return iapImMessageService.getDefaultFilter(iapImMessageDto);
    }

    /**
     * 创建群聊
     *
     * @param groupMemberList 群成员列表
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public IapImMessageListDto createGroup(List<IapImGroupMemberDto> groupMemberList) {
        String userName = userUtil.getUser().getUserName();
        IapImGroup iapImGroup = new IapImGroup();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        iapImGroup.setId(SnowIdUtil.getLongId());
        iapImGroup.setFounder(userName);
        iapImGroup.setTimestamp(date.getTime());
        iapImGroup.setSummary("默认简介");
        iapImGroup.setName(userName + "新群聊" + dateFormat.format(date));
        // 创建群
        iapImGroupMapper.insert(iapImGroup);
        // 创建群成员
        return this.createGroupMember(groupMemberList, iapImGroup);

    }


    /**
     * 添加群成员
     *
     * @param groupId    群ID
     * @param memberList 需要添加的群成员列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addGroupMember(String groupId, List<IapImGroupMemberDto> memberList) {
        String userName = userUtil.getUser().getUserName();
        List<IapImGroupMember> allGroupList = iapImGroupMemberMapper.selectList(new QueryWrapper<IapImGroupMember>().eq("group_id", groupId));
        allGroupList.stream().forEach(x -> {
            memberList.removeIf(usr -> usr.getAccount().equals(x.getAccount()));
        });
        if (!memberList.isEmpty()) {
            List<IapImGroupMember> iapImGroupMembers = new ArrayList<>();
            List<IapImMessageUser> iapImMessageUsers = new ArrayList<>();
            StringBuilder stringBuilder = new StringBuilder();
            for (IapImGroupMemberDto iapImGroupMemberDto : memberList) {
                iapImGroupMemberDto.setId(SnowIdUtil.getLongId());
                iapImGroupMemberDto.setGroupId(groupId);
                iapImGroupMemberDto.setHost(host);
                iapImGroupMembers.add(DtoUtils.convertObj(iapImGroupMemberDto, IapImGroupMember.class));
                IapImMessageUser iapImMessageUser = new IapImMessageUser();
                iapImMessageUser.setId(SnowIdUtil.getLongId());
                iapImMessageUser.setSender(groupId);
                iapImMessageUser.setReceiver(iapImGroupMemberDto.getAccount());
                iapImMessageUser.setListType(Constants.MessageUserDto.LIST_TYPE1);
                iapImMessageUser.setShowType(Constants.MessageUserDto.SHOW_TYPE0);
                iapImMessageUser.setTopTime(Constants.GroupMember.MUTE_TIME_DEFAULT);
                iapImMessageUsers.add(iapImMessageUser);
                iapImMessageUsers.add(MessageUtil.convertUser(iapImMessageUser));
                stringBuilder.append(iapImGroupMemberDto.getAccount()).append(",");
            }
            iapImGroupMemberMapper.insertList(iapImGroupMembers);
            iapImMessageUserMapper.insertList(iapImMessageUsers);
            IapImMessageDto iapImMessageDto = new IapImMessageDto();
            iapImMessageDto.setSender(userName);
            iapImMessageDto.setReceiver(groupId);
            iapImMessageDto.setContent("用户" + userName + "邀请" + stringBuilder.toString() + "+进入群");
            iapImMessageDto.setAction(Constants.MessageAction.ACTION_103);
            iapImMessageDto.setState(Constants.Message.STATE_READ);
            iapImMessageDto.setFormat(Constants.MessageFormat.FORMAT_TEXT);
            // 通知群成员
            this.sendMessage(iapImMessageDto);
        }
    }


    /**
     * 添加群成员
     *
     * @param memberList
     * @param iapImGroup
     * @return
     */
    private IapImMessageListDto createGroupMember(List<IapImGroupMemberDto> memberList, IapImGroup iapImGroup) {
        Date date = new Date();
        List<IapImGroupMember> iapImGroupMembers = new ArrayList<>();
        IapImGroupMember iapImGroupMember = new IapImGroupMember();
        iapImGroupMember.setAccount(iapImGroup.getFounder());
        iapImGroupMember.setGroupId(iapImGroup.getId());
        iapImGroupMember.setHost(host);
        iapImGroupMember.setTimestamp(date.getTime());
        iapImGroupMember.setMuteType(Constants.GroupMember.MUTE_TYPE0);
        iapImGroupMember.setMuteTime(Constants.GroupMember.MUTE_TIME_DEFAULT);
        iapImGroupMembers.add(iapImGroupMember);
        memberList.removeIf(usr -> usr.getAccount().equals(iapImGroup.getFounder()));
        memberList.forEach(x -> {
            x.setId(SnowIdUtil.getLongId());
            x.setHost(host);
            x.setTimestamp(date.getTime());
            x.setGroupId(iapImGroup.getId());
            x.setMuteType(Constants.GroupMember.MUTE_TYPE0);
            x.setMuteTime(Constants.GroupMember.MUTE_TIME_DEFAULT);
            iapImGroupMembers.add(DtoUtils.convertObj(x, IapImGroupMember.class));
        });
        iapImGroupMemberMapper.insertList(iapImGroupMembers);
        List<IapImMessageUser> iapImMessageUsers = this.convertGroupUser(iapImGroupMembers);
        // 保存到消息用户
        iapImMessageUserMapper.insertList(iapImMessageUsers);
        // 封装消息返回体
        return returnPacket(iapImMessageUsers, iapImGroupMembers, iapImGroup);
    }

    /**
     * 创建群聊  返回消息体封装
     *
     * @param iapImMessageUsers
     * @param iapImGroup
     * @return
     */
    private IapImMessageListDto returnPacket(List<IapImMessageUser> iapImMessageUsers, List<IapImGroupMember> iapImGroupMembers, IapImGroup iapImGroup) {
        UserTDto user = userUtil.getUser();
        String userName = user.getUserName();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date();
        IapImMessageDto iapImMessageDto = new IapImMessageDto();
        iapImMessageDto.setSender(iapImGroup.getFounder());
        iapImMessageDto.setReceiver(iapImGroup.getId());
        iapImMessageDto.setContent("用户" + userName + dateFormat.format(date) + "创建群聊");
        iapImMessageDto.setAction(Constants.MessageAction.ACTION_102);
        iapImMessageDto.setState(Constants.Message.STATE_READ);
        iapImMessageDto.setFormat(Constants.MessageFormat.FORMAT_TEXT);
        iapImMessageDto.setSenderRealName(userName);
        iapImMessageDto.setGroupName(iapImGroup.getName());
        iapImMessageDto.setSendAvatar(user.getAvatar());
        // 消息推送
        this.sendMessage(iapImMessageDto);
        // 创建消息返回体
        IapImMessageListDto iapImMessageListDto = new IapImMessageListDto();
        List<IapImMessageDto> listDtos = new ArrayList<>();
        listDtos.add(iapImMessageDto);
        iapImMessageListDto.setIapImMessageDtos(listDtos);
        iapImMessageListDto.setReceiver(iapImGroup.getId());
        iapImMessageListDto.setSender(iapImGroup.getFounder());
        iapImMessageListDto.setGroupName(iapImGroup.getName());
        iapImMessageUsers.forEach(x -> {
            if (x.getSender().equals(iapImMessageDto.getSender())) {
                iapImMessageListDto.setId(x.getId());
                iapImMessageListDto.setListType(x.getListType());
            }
        });
        iapImMessageListDto.setGroupMemberList(iapImGroupMembers);
        return iapImMessageListDto;
    }

    /**
     * 判断是否有发送权限以及禁言
     *
     * @param iapImMessageDto
     * @return false 没有发言权 或者被禁言
     */
    private boolean onSendMessage(IapImMessageDto iapImMessageDto) {
        IapImGroupMember iapImGroupMember = iapImGroupMemberMapper.selectOne(new QueryWrapper<IapImGroupMember>().eq("group_id", iapImMessageDto.getReceiver()).eq("account", iapImMessageDto.getSender()));
        // 群成员不存在
        if (iapImGroupMember == null) {
            return false;
        }
        // 判断是否被禁言和过期时间
        if (iapImGroupMember.getMuteType() != null && iapImGroupMember.getMuteType().equals(Constants.GroupMember.MUTE_TYPE1)) {
            // 过期时间已过
            if (iapImGroupMember.getMuteTime() <= System.currentTimeMillis()) {
                // 更新禁言时间
                iapImGroupMember.setMuteType(Constants.GroupMember.MUTE_TYPE0).setMuteTime(Constants.GroupMember.MUTE_TIME_DEFAULT);
                iapImGroupMemberMapper.updateById(DtoUtils.convertObj(iapImGroupMember, IapImGroupMember.class));
                return true;
            }
            return false;
        }
        return true;
    }

    /**
     * 批量转换 sender 和 receiver 的位置
     *
     * @param iapImGroupMemberList
     * @return
     */
    private List<IapImMessageUser> convertGroupUser(List<IapImGroupMember> iapImGroupMemberList) {
        List<IapImMessageUser> newImMessageUserList = new ArrayList<>();
        if (!iapImGroupMemberList.isEmpty()) {
            for (IapImGroupMember iapImGroupMember : iapImGroupMemberList) {
                IapImMessageUser newIapImMessageUser = new IapImMessageUser();
                newIapImMessageUser.setId(SnowIdUtil.getLongId());
                newIapImMessageUser.setSender(iapImGroupMember.getGroupId());
                newIapImMessageUser.setReceiver(iapImGroupMember.getAccount());
                newIapImMessageUser.setListType(Constants.MessageUserDto.LIST_TYPE1);
                newIapImMessageUser.setShowType(Constants.MessageUserDto.SHOW_TYPE0);
                newIapImMessageUser.setTopTime(Constants.GroupMember.MUTE_TIME_DEFAULT);
                newImMessageUserList.add(newIapImMessageUser);
                newImMessageUserList.add(MessageUtil.convertUser(newIapImMessageUser));
            }
        }
        return newImMessageUserList;
    }

    /**
     * 保存群消息
     *
     * @param oldIapImessageDto 群成员主动发送的消息
     * @return
     */
    @Override
    public List<IapImMessage> saveGroupMessages(IapImMessageDto oldIapImessageDto) {
        IapImMessageDto iapImMessageDto = DtoUtils.convertObj(oldIapImessageDto, IapImMessageDto.class);
        List<IapImMessage> listMessage = new ArrayList<>();
        List<IapImGroupMember> iapImGroupMembers = iapImGroupMemberMapper.selectList(new QueryWrapper<IapImGroupMember>().eq("group_id", oldIapImessageDto.getReceiver()).ne("account", oldIapImessageDto.getSender()));
        iapImMessageDto.setAction(Constants.Message.ACTION_1);
        listMessage.add(DtoUtils.convertObj(iapImMessageDto, IapImMessage.class));
        iapImGroupMembers.parallelStream().forEach(mes -> {
            IapImMessage iapImMessage = DtoUtils.convertObj(iapImMessageDto, IapImMessage.class);
            iapImMessage.setReceiver(mes.getAccount());
            iapImMessage.setSender(oldIapImessageDto.getReceiver());
            iapImMessage.setAction(Constants.Message.ACTION_3);
            iapImMessage.setId(SnowIdUtil.getLongId());
            listMessage.add(iapImMessage);
        });
        return listMessage;
    }


    /**
     * 像指定人发送消息
     *
     * @param user 用户
     */
    private void sendSenderList(String user) {
        threadPoolExpansion.submit(() -> {
            messageListTemplatePusher.sendMessage(iapImMessageService.msgAll(user), user);
        });
    }


    /**
     * 发送群消息
     *
     * @param userList 用户列表
     */
    private void sendGroupMessage(List<String> userList) {
        for (String user : userList) {
            this.sendSenderList(user);
        }
    }
}
