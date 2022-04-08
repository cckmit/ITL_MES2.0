package com.itl.im.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.itl.im.provider.mapper.IapImGroupMapper;
import com.itl.im.provider.mapper.IapImMessageUserMapper;
import com.itl.im.provider.util.SnowIdUtil;
import iap.im.api.dto.IapImGroupDto;
import iap.im.api.entity.IapImMessage;
import iap.im.api.entity.IapImMessageUser;
import iap.im.api.service.IapImMessageUserService;
import iap.im.api.variable.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 消息列表Service层实现类
 *
 * @author mjl
 * @date 2020-10-10
 * @since jdk1.8
 */
@Service
@Slf4j
public class IapImMessageUserServiceImpl extends ServiceImpl<IapImMessageUserMapper, IapImMessageUser> implements IapImMessageUserService {

    @Resource
    private IapImGroupMapper iapImGroupMapper;

    @Resource
    private IapImMessageUserMapper iapImMessageUserMapper;

    /**
     * 更新
     *
     * @param iapImMessageUser
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateIapImMessageUser(IapImMessageUser iapImMessageUser) {

        IapImMessageUser iapImMessageUserInDb = iapImMessageUserMapper.selectById(iapImMessageUser.getId());
        // 该操作为已退出群聊的人删除了该消息列表，直接删除该消息列表
        if (iapImMessageUserInDb.getShowType() == Constants.MessageUserDto.SHOW_TYPE3 && iapImMessageUserInDb.getListType() == Constants.MessageUserDto.LIST_TYPE1) {
            return iapImMessageUserMapper.removeItemBySenderAndReceiver(iapImMessageUserInDb.getSender(), iapImMessageUserInDb.getReceiver());
        }
        return this.updateById(iapImMessageUser);
    }

    /**
     * 通过 sender和 receiver 双向删除
     *
     * @param sender
     * @param receiver
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean removeDoubleMessageUserItem(String sender, String receiver, Short listType) {

        if (StringUtils.isNoneBlank(sender, receiver)) {
            iapImMessageUserMapper.removeItemBySenderAndReceiver(sender, receiver);
            return true;
        }
        return false;
    }

    /**
     * 批量双向添加群成员到 iap_im_message_user_t 表
     *
     * @param groupMemberList
     * @param groupId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean addGroupMessageUserItem(List<String> groupMemberList, String groupId) {

        if (!groupMemberList.isEmpty() && StringUtils.isNoneBlank(groupId)) {
            List<IapImMessageUser> insertIapImMessageUserList = Lists.newArrayList();
            for (String groupMemberUsername : groupMemberList) {
                IapImMessageUser iapImMessageUser = new IapImMessageUser();
                iapImMessageUser.setId(SnowIdUtil.getLongId());
                iapImMessageUser.setSender(groupMemberUsername).setReceiver(groupId);
                iapImMessageUser.setShowType(Constants.MessageUserDto.SHOW_TYPE0);
                iapImMessageUser.setListType(Constants.MessageUserDto.LIST_TYPE1);
                insertIapImMessageUserList.add(iapImMessageUser);
            }
//            insertIapImMessageUserList.addAll(this.convertIapImMessageUser(insertIapImMessageUserList));
            // 双向添加
            return false;
        }
        return false;
    }

    /**
     * 批量双向删除群成员到 iap_im_message_user_t 表
     *
     * @param groupMemberList
     * @param groupId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean removeGroupMessageUserItem(List<String> groupMemberList, String groupId) {
        if (!groupMemberList.isEmpty() && StringUtils.isNoneBlank(groupId)) {
            return iapImMessageUserMapper.removeGroupMemberByGroupId(groupMemberList, groupId);
        }
        return false;
    }

    /**
     * 通过传入的消息判断这个子项在不在 iap_im_message_user_t 表，如果不存在，则新增该子项
     *
     * @param iapImMessage
     */
    @Override
    public void saveOrUpdateMessageUserItem(IapImMessage iapImMessage) {
        String action = iapImMessage.getAction();
        if (Constants.Message.ACTION_0.equals(action)) {
            // 人
            IapImMessageUser iapImMessageUser = iapImMessageUserMapper.selectOne(new QueryWrapper<IapImMessageUser>().eq("sender", iapImMessage.getSender()).eq("receiver", iapImMessage.getReceiver()));
            if (iapImMessageUser == null) {
                // 新增到数据库
                iapImMessageUser = new IapImMessageUser();
                iapImMessageUser.setId(SnowIdUtil.getLongId()).setSender(iapImMessage.getSender()).setReceiver(iapImMessage.getReceiver()).setListType(Constants.MessageUserDto.LIST_TYPE0).setShowType(Constants.MessageUserDto.SHOW_TYPE0);
                this.saveOrUpdate(iapImMessageUser);
            } else if (iapImMessageUser.getShowType() == Constants.MessageUserDto.SHOW_TYPE1) {
                this.saveOrUpdate(iapImMessageUser.setShowType(Constants.MessageUserDto.SHOW_TYPE0));
            }
        } else if (Constants.Message.ACTION_1.equals(action)) {
            // 群
            IapImGroupDto iapImGroupDto = iapImGroupMapper.getAllGroupWithGroupMemberList(iapImMessage.getReceiver());
            // 群成员数量和展示的数量不一致时，更新该群的消息列表的信息
            if (!iapImGroupDto.getGroupMemberNum().equals(iapImGroupDto.getShowTrueNum())) {
                iapImMessageUserMapper.updateShowTypeByGroupId(iapImMessage.getReceiver(), Constants.MessageUserDto.SHOW_TYPE0);
            }
        }
    }


}
