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
package iap.im.api.entity;

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
 * 实体类-群成员（iap_im_group_member_t）
 *
 * @author mjl
 * @date 2020-10-10
 * @since jdk1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("iap_im_group_member_t")
@Accessors(chain = true)
public class IapImGroupMember implements Serializable {

    private transient static final long serialVersionUID = 4733464888738356502L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 群ID
     */
    @TableField("group_id")
    private String groupId;

    /**
     * 用户名（usernmae）
     */
    @TableField("account")
    private String account;

    /**
     * 群成员类别标识（0-群成员，1-群主）
     */
    @TableField("host")
    private String host;

    /**
     * 入群时间
     */
    @TableField("timestamp")
    private Long timestamp;

    /**
     * 禁言状态  0-未被禁言，  1-已被禁言，默认为 0
     */
    @TableField("mute_type")
    private Short muteType;

    /**
     * 禁言到期时间 ， 存入的值是禁言到期时间的时间戳，默认为 0
     */
    @TableField("mute_time")
    private Long muteTime;

    /**
     * 创建人
     */
    @TableField("creater")
    private String creater;

    /**
     * 创建时间
     */
    @TableField("create_date")
    private Date createDate;

    /**
     * 更新人
     */
    @TableField("last_update_by")
    private String lastUpdateBy;

    /**
     * 最后更新时间
     */
    @TableField("last_update_date")
    private Date lastUpdateDate;

}
