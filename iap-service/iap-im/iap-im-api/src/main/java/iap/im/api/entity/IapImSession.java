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
package iap.im.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.google.protobuf.InvalidProtocolBufferException;
import iap.im.api.proto.SessionProto;
import iap.im.api.sendDto.Transportable;
import iap.im.api.variable.CIMConstant;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.net.SocketAddress;
import java.util.Date;
import java.util.Objects;

/**
 * 实体类-ImSession类（iap_im_session_t）
 *
 * @author tanq
 * @date 2020-10-10
 * @since jdk1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("iap_im_session_t")
@Accessors(chain = true)
public class IapImSession implements Serializable {

    /**
     * 数据库主键ID
     */
    @TableId(type = IdType.UUID)
    private String id;

    /**
     * session绑定的用户账号
     */
    @TableField("account")
    private String account;

    /**
     * session在本台服务器上的ID
     */
    @TableField("nid")
    private String nid;

    /**
     * 客户端ID (设备号码+应用包名),ios为deviceToken
     */
    @TableField("device_id")
    private String deviceId;

    /**
     * session绑定的服务器IP
     */
    @TableField("host")
    private String host;

    /**
     * 终端设备类型
     */
    @TableField("channel")
    private String channel;

    /**
     * 终端设备型号
     */
    @TableField("device_model")
    private String deviceModel;

    /**
     * 终端应用版本
     */
    @TableField("client_version")
    private String clientVersion;

    /**
     * 终端系统版本
     */
    @TableField("system_version")
    private String systemVersion;

    /**
     * 登录时间
     */
    @TableField("bind_time")
    private Long bindTime;

    /**
     * 经度
     */
    @TableField("longitude")
    private Double longitude;

    /**
     * 维度
     */
    @TableField("latitude")
    private Double latitude;

    /**
     * 位置
     */
    @TableField("location")
    private String location;

    /**
     * APNs推送状态
     */
    @TableField("apns")
    private int apns;

    /**
     * 状态
     */
    @TableField("state")
    private int state;

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
