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
package iap.im.api.dto;

import com.itl.iap.system.api.dto.IapSysUserTDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * DTO类-群组（iap_im_group_t）
 *
 * @author mjl
 * @date 2020-10-10
 * @since jdk1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class IapImGroupDto implements Serializable {

    /**
     * 主键ID
     */
    private String id;

    /**
     * 群组名称
     */
    private String name;

    /**
     * 群简介
     */
    private String summary;

    /**
     * 群类别
     */
    private String category;

    /**
     * 群创建者（群主-username）
     */
    private String founder;

    /**
     * 群创建时间
     */
    private Long timestamp;
    /**
     * 创建人
     */
    private String creater;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 最后修改人
     */
    private String lastUpdateBy;

    /**
     * 最后修改时间
     */
    private Date lastUpdateDate;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 群成员数量
     */
    private Integer groupMemberNum;

    /**
     * 消息列表展示为0（显示）的群成员数量
     */
    private Integer showTrueNum;

    /**
     * 群成员列表
     */
    private List<IapImGroupMemberDto> memberList;

    /**
     * 用户列表
     */
    private List<IapSysUserTDto> userList;
}
