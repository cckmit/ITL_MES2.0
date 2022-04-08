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


import iap.im.api.entity.IapImGroup;
import iap.im.api.entity.IapImGroupMember;

import java.util.List;

/**
 * 群组Service层
 *
 * @author mjl
 * @date 2020-10-10
 * @since jdk1.8
 */
public interface IapImGroupService {

    /**
     * 解散群聊
     *
     * @param selfAccount 操作者（自己的账号）
     * @param groupId     群ID
     * @return boolean
     */
    boolean removeGroup(String selfAccount, String groupId);

    /**
     * 更新群信息
     *
     * @param iapImGroup 需要更新的群
     * @return boolean
     */
    boolean updateGroupInfo(IapImGroup iapImGroup);

    /**
     * 查询与我相关的所有群列表
     *
     * @param selfAccount 操作者（自己的账号）
     * @return List<IapImGroup>
     */
    List<IapImGroup> getMyAllGroupList(String selfAccount);
}
