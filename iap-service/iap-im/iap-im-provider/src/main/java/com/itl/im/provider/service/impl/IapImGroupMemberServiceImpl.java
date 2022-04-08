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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.im.provider.mapper.IapImGroupMapper;
import com.itl.im.provider.mapper.IapImGroupMemberMapper;
import com.itl.im.provider.mapper.IapImMessageUserMapper;
import com.itl.im.provider.util.SnowIdUtil;
import iap.im.api.dto.IapImGroupDto;
import iap.im.api.entity.IapImGroup;
import iap.im.api.entity.IapImGroupMember;
import iap.im.api.entity.IapImMessageUser;
import iap.im.api.service.IapImGroupMemberService;
import iap.im.api.service.IapImMessageUserService;
import iap.im.api.variable.Constants;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 群成员Service实现类
 *
 * @author mjl
 * @date 2020-10-10
 * @since jdk1.8
 */
@Service
public class IapImGroupMemberServiceImpl extends ServiceImpl<IapImGroupMemberMapper, IapImGroupMember> implements IapImGroupMemberService {

    @Resource
    private IapImMessageUserService iapImMessageUserService;

    @Resource
    private IapImGroupMapper iapImGroupMapper;

    @Resource
    private IapImGroupMemberMapper iapImGroupMemberMapper;

    @Resource
    private IapImMessageUserMapper iapImMessageUserMapper;

    /**
     * 修改是否禁用群聊天
     *
     * @param iapImGroupMemberList 需要更新的群成员列表
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateMuteUser(List<IapImGroupMember> iapImGroupMemberList) {
        if (!iapImGroupMemberList.isEmpty()) {
            iapImGroupMemberList.forEach(x -> {
                x.setTimestamp(new Date().getTime());
            });
            return this.updateBatchById(iapImGroupMemberList);
        }
        return false;
    }

    /**
     * 批量删除群成员
     *
     * @param groupMemberAccountList 需要删除的群成员用户名（username)
     * @param groupId                群ID
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeBatch(List<String> groupMemberAccountList, String groupId) {

        if (groupId != null && !groupMemberAccountList.isEmpty()) {
            // 删除群成员表
            iapImGroupMemberMapper.removeGroupMemeberBatch(groupMemberAccountList, groupId);
            // 更改 message_user 中级表 show_type 变为 3
//            iapImMessageUserMapper.removeGroupMemberByGroupId(groupMemberAccountList, groupId);
            iapImMessageUserMapper.updateList(groupId, groupMemberAccountList, Constants.MessageUserDto.SHOW_TYPE3);
            return true;
        }
        return false;
    }

    /**
     * 通过群ID查询该群的群成员列表
     *
     * @param groupId 群ID
     * @return IapImGroupDto
     */
    @Override
    public IapImGroupDto selectGroupMemberListByGroupId(String groupId) {
        return iapImGroupMapper.getAllGroupWithGroupMemberList(groupId);
    }

    /**
     * 主动离开某个群
     *
     * @param selfAccount 自己的账号（登录人）
     * @param groupId     群ID
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean leaveGroup(String selfAccount, String groupId) {

        IapImGroupMember selfMember = iapImGroupMemberMapper.selectOne(new QueryWrapper<IapImGroupMember>().eq("group_id", groupId).eq("account", selfAccount));

        // 判断该用户是否在本群中
        if (selfMember != null) {
            // 判断该用户是否为群主
            if (Constants.GroupMember.RULE_FOUNDER.equals(selfMember.getHost())) {
                // 如果群成员数量大于1则转让群主给下一位群成员，否则解散群
                IapImGroupDto iapImGroupDto = iapImGroupMapper.getAllGroupWithGroupMemberList(groupId);
                if (iapImGroupDto.getMemberList().size() > 1) {
                    iapImGroupMemberMapper.updateGroupHostToNext(groupId);
                } else {
                    // 解散群
                    // 删除群成员
                    iapImGroupMemberMapper.delete(new QueryWrapper<IapImGroupMember>().eq("group_id", groupId));
                    // 删除群
                    iapImGroupMapper.delete(new QueryWrapper<IapImGroup>().eq("id", groupId));
                    // 改变消息列表状态
                    iapImMessageUserMapper.update(new IapImMessageUser().setShowType(Constants.MessageUserDto.SHOW_TYPE3), new UpdateWrapper<IapImMessageUser>().eq("receiver", groupId).or().eq("sender", groupId));
                    return true;
                }
            }
            // 删除 iap_im_group_member_t 表
            iapImGroupMemberMapper.deleteById(selfMember.getId());
            // 改变消息列表状态
//            iapImMessageUserMapper.update(new IapImMessageUser().setShowType(Constants.MessageUserDto.SHOW_TYPE3), new UpdateWrapper<IapImMessageUser>().eq("sender", selfMember.getAccount()).eq("receiver", selfMember.getGroupId()));
//            iapImMessageUserMapper.update(new IapImMessageUser().setShowType(Constants.MessageUserDto.SHOW_TYPE3), new UpdateWrapper<IapImMessageUser>().eq("receiver", selfMember.getAccount()).eq("sender", selfMember.getGroupId()));
            iapImMessageUserMapper.updateList(selfMember.getGroupId(), Arrays.asList(selfAccount), Constants.MessageUserDto.SHOW_TYPE3);
            return true;
        }
        return false;
    }

}
