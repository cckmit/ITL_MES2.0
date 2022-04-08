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
import com.google.common.collect.Lists;
import com.itl.im.provider.mapper.IapImGroupMapper;
import com.itl.im.provider.mapper.IapImGroupMemberMapper;
import com.itl.im.provider.mapper.IapImMessageUserMapper;
import com.itl.im.provider.util.SnowIdUtil;
import iap.im.api.dto.IapImMessageDto;
import iap.im.api.entity.IapImGroup;
import iap.im.api.entity.IapImGroupMember;
import iap.im.api.entity.IapImMessageUser;
import iap.im.api.service.IapImGroupMessageService;
import iap.im.api.service.IapImGroupService;
import iap.im.api.service.IapImMessageUserService;
import iap.im.api.variable.Constants;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 群Service实现类
 *
 * @author mjl
 * @date 2020-10-10
 * @since jdk1.8
 */
@Service
public class IapImGroupServiceImpl extends ServiceImpl<IapImGroupMapper, IapImGroup> implements IapImGroupService {

    @Resource
    private IapImGroupMapper iapImGroupMapper;

    @Resource
    private IapImGroupMemberMapper iapImGroupMemberMapper;

    @Resource
    private IapImMessageUserMapper iapImMessageUserMapper;

    /**
     * 解散群聊
     *
     * @param selfAccount 操作者（自己的账号）
     * @param groupId     群ID
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeGroup(String selfAccount, String groupId) {
        if (selfAccount != null & groupId != null) {
            // 当前操作的用户是该群的群主
            IapImGroupMember iapImGroupMember = iapImGroupMemberMapper.selectOne(new QueryWrapper<IapImGroupMember>()
                    .eq("group_id", groupId).eq("account", selfAccount).eq("host", Constants.GroupMember.RULE_FOUNDER));
            if (iapImGroupMember != null) {
                // 删除群的所有消息
//                iapImMessageMapper.delete(new QueryWrapper<IapImMessage>().eq("receiver", groupId).or().eq("sender", groupId));
                // 删除message_user中间表
//                iapImMessageUserMapper.delete(new QueryWrapper<IapImMessageUser>().eq("sender", groupId).or().eq("receiver", groupId));

                // 删除群成员
                iapImGroupMemberMapper.delete(new QueryWrapper<IapImGroupMember>().eq("group_id", groupId));
                // 删除群
                iapImGroupMapper.delete(new QueryWrapper<IapImGroup>().eq("id", groupId));
                // 改变消息列表状态
                iapImMessageUserMapper.update(new IapImMessageUser().setShowType(Constants.MessageUserDto.SHOW_TYPE3),
                        new UpdateWrapper<IapImMessageUser>().eq("receiver", groupId).or().eq("sender", groupId));
//                iapImMessageUserMapper.delete(new QueryWrapper<IapImMessageUser>().eq("sender", groupId));

                return true;
            }
        }
        return false;
    }

    /**
     * 更新群信息
     *
     * @param iapImGroup 需要更新的群
     * @return boolean
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateGroupInfo(IapImGroup iapImGroup) {
        return this.updateById(iapImGroup);
    }

    /**
     * 查询与我相关的所有群列表
     *
     * @param selfAccount 操作者（自己的账号）
     * @return List<IapImGroup>
     */
    @Override
    public List<IapImGroup> getMyAllGroupList(String selfAccount) {
        return iapImGroupMapper.getMyAllGroupList(selfAccount);
    }

}
