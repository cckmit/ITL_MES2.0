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
import iap.im.api.entity.IapImGroupMember;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 群成员Mapper
 *
 * @author mjl
 * @date 2020-10-10
 * @since jdk1.8
 */
public interface IapImGroupMemberMapper extends BaseMapper<IapImGroupMember> {

    /**
     * 批量插入群成员
     *
     * @param insertNewMemberList
     */
    Boolean insertList(@Param("list") List<IapImGroupMember> insertNewMemberList);

    /**
     * 通过群ID查询该群的群成员列表
     *
     * @param groupId
     * @return
     */
    List<IapImGroupMember> selectGroupMemberListByGroupId(@Param("groupId") String groupId);

    /**
     * 批量删除群成员
     *
     * @param groupMemberAccountList
     * @param groupId
     * @return
     */
    boolean removeGroupMemeberBatch(@Param("groupMemberAccountList") List<String> groupMemberAccountList, @Param("groupId") String groupId);

    /**
     * 给下一位群成员当群主
     * @param groupId
     * @return
     */
    boolean updateGroupHostToNext(@Param("groupId")String groupId);
}
