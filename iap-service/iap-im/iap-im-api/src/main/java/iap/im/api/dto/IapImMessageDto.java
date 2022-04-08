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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import iap.im.api.proto.MessageProto;
import iap.im.api.sendDto.Transportable;
import iap.im.api.variable.CIMConstant;
import iap.im.api.variable.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO类-消息（iap_im_group_t）
 *
 * @author 李骐光
 * @date 2020-10-10
 * @since jdk1.8
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class IapImMessageDto implements Serializable {

    /**
     * 分页对象
     */
    private Page page;

    /**
     * 主键ID
     */
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 消息发送人
     */
    @TableField("sender")
    private String sender;

    /**
     * 消息接收人
     */
    @TableField("receiver")
    private String receiver;

    /**
     * 消息标题
     */
    @TableField("title")
    private String title;

    /**
     * 消息内容
     */
    @TableField("content")
    private String content;

    /**
     * 扩展字段
     */
    @TableField("extra")
    private String extra;

    /**
     * 消息行为（0 普通消息 1 群消息发送 3 群消息接收，其他类型见Constants类）
     */
    @TableField("action")
    private String action;

    /**
     * 消息状态（0: 未发送 1：已发送 2：已接收 3：已查看 4:已删除）
     */
    @TableField("state")
    private String state;

    /**
     * 消息格式（0：文字，1：图片，2：语言，3：文件，4：地图, 5：视频, 6: 卡片）
     */
    @TableField("format")
    private String format;

    /**
     * 消息发送时间
     */
    @TableField("timestamp")
    private Long timestamp;

    /**
     * 更改用户聊天框展示用户下不在显示消息 hideMessage 等于当前用户(username)或all 代表隐藏
     */
    @TableField("hide_message")
    private String hideMessage;

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
     * 更新时间
     */
    @TableField("last_update_date")
    private Date lastUpdateDate;

    /**
     * 群组名称
     */
    private String groupName;

    /**
     * 发送人头像
     */
    private String sendAvatar;

    /**
     * 接收人头像
     */
    private String receiverAvatar;

    /**
     * 发送人名称
     */
    private String senderRealName;

    /**
     * 接收人名称
     */
    private String receiverRealName;

    /**
     * 消息读取数，用来统计群消息有几人读取或用户消息的未读数量
     */
    private int messageNumber;

    /**
     * 群成员数，用来判断群消息是否全部已读
     */
    private int groupNumber;

    public IapImMessageDto() {
        state = Constants.Message.STATE_NOT_RECEIVED;
        format = Constants.MessageFormat.FORMAT_TEXT;
        timestamp = System.currentTimeMillis();
    }

    // 是否为动作消息，无需记录，无需显示
    @JsonIgnore
    public boolean isActionMessage() {
        return action.startsWith("9");
    }


}
