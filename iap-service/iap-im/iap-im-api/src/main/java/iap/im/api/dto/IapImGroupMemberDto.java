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

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO类-群成员（iap_im_group_member_t）
 *
 * @author mjl
 * @date 2020-10-10
 * @since jdk1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class IapImGroupMemberDto implements Serializable {

    /**
     * 主键ID
     */
    private String id;

    /**
     * 群ID
     */
    private String groupId;

    /**
     * 用户名（usernmae）
     */
    private String account;

    /**
     * 群成员类别标识（0-群成员，1-群主）
     */
    private String host;

    /**
     * 入群时间
     */
    private Long timestamp;

    /**
     * 禁言状态  0-未被禁言，  1-已被禁言，默认为 0
     */
    private Short muteType;

    /**
     * 禁言到期时间 ， 存入的值是禁言到期时间的时间戳，默认为 0
     */
    private Long muteTime;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新人
     */
    private String lastUpdateBy;

    /**
     * 最后更新时间
     */
    private Date lastUpdateDate;

    /**
     * 以下为用户信息
     */
    private String userId;
    private String username;
    private String realName;
    private String nickName;
    private String userMobile;
    private String email;
    private String userAvatar;
    private Short state;
    private Date validity;


}
