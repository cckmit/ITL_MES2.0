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

import iap.im.api.dto.IapImGroupDto;
import iap.im.api.entity.IapImGroupMember;

import java.util.List;

/**
 * 群成员Service层
 *
 * @author mjl
 * @date 2020-10-10
 * @since jdk1.8
 */
public interface IapImGroupMemberService {

    /**
     * 修改是否禁用群聊天
     *
     * @param iapImGroupMemberList 需要更新的群成员列表
     * @return boolean
     */
    boolean updateMuteUser(List<IapImGroupMember> iapImGroupMemberList);

    /**
     * 批量删除群成员
     *
     * @param groupMemberAccountList 需要删除的群成员用户名（username)
     * @param groupId 群ID
     * @return boolean
     */
    boolean removeBatch(List<String> groupMemberAccountList, String groupId);

    /**
     * 通过群ID查询该群的群成员列表
     *
     * @param groupId 群ID
     * @return IapImGroupDto
     */
    IapImGroupDto selectGroupMemberListByGroupId(String groupId);

    /**
     * 主动离开某个群
     *
     * @param selfAccount 自己的账号（登录人）
     * @param groupId 群ID
     * @return
     */
    Boolean leaveGroup(String selfAccount, String groupId);
}
