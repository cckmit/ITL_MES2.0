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
import iap.im.api.dto.IapImGroupDto;
import iap.im.api.entity.IapImGroup;
import iap.im.api.entity.IapImGroupMember;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 群组Mapper
 *
 * @author mjl
 * @date 2020-10-10
 * @since jdk1.8
 */
public interface IapImGroupMapper extends BaseMapper<IapImGroup> {

    /**
     * 查询与我相关的所有群列表
     *
     * @param selfAccount 登录者（自己）
     * @return
     */
    List<IapImGroup> getMyAllGroupList(@Param("selfAccount") String selfAccount);

    /**
     * 获取所有群并且附带群成员
     * @param groupId 群ID
     * @return
     */
    IapImGroupDto getAllGroupWithGroupMemberList(@Param("groupId") String groupId);
}
