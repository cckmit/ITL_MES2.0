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
import com.itl.iap.system.api.dto.IapSysUserTDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 实体类-群组（iap_im_group_t）
 *
 * @author mjl
 * @date 2020-10-10
 * @since jdk1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("iap_im_group_t")
@Accessors(chain = true)
public class IapImGroup implements Serializable {

    private transient static final long serialVersionUID = 4733464888738356502L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 群名称
     */
    @TableField("name")
    private String name;

    /**
     * 群简介
     */
    @TableField("summary")
    private String summary;

    /**
     * 群类别
     */
    @TableField("category")
    private String category;

    /**
     * 群创建者（群主-username）
     */
    @TableField("founder")
    private String founder;

    /**
     * 群创建时间
     */
    @TableField("timestamp")
    private Long timestamp;

    /**
     * 群头像
     */
    @TableField("avatar")
    private String avatar;

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
