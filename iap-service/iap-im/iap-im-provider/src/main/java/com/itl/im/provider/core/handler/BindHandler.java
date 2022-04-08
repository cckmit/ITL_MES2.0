/*
 * Copyright © 2017 海通安恒科技有限公司.
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
 *                        Website : http://www.htah.com.cn/                           *
 *                                                                                     *
 ***************************************************************************************
 */
package com.itl.im.provider.core.handler;

import com.itl.im.provider.core.push.MessageListTemplatePusher;
import com.itl.im.provider.core.push.provider.MessageTemplate;
import iap.im.api.dto.IapImMessageDto;
import iap.im.api.service.IapImIMSessionService;
import iap.im.api.variable.CIMConstant;
import com.itl.im.provider.core.netty.handler.CIMRequestHandler;
import iap.im.api.sendDto.IapImSessionDto;
import iap.im.api.sendDto.ReplyBody;
import iap.im.api.sendDto.SentBody;
import iap.im.api.variable.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 客户长连接 账户绑定实现
 */
@Component
public class BindHandler implements CIMRequestHandler {

    @Value("#{'${server.host}'.trim()}")
    private String host;

    @Resource
    private IapImIMSessionService iapImImSessionService;

 /*   @Resource
    private DefaultMessagePusher defaultMessagePusher;*/
    @Resource
    private MessageListTemplatePusher messageListTemplatePusher;

    @Override
    public void process(IapImSessionDto newSession, SentBody body) {

        ReplyBody reply = new ReplyBody();
        reply.setKey(body.getKey());
        reply.setCode(HttpStatus.OK.value());
        reply.setTimestamp(System.currentTimeMillis());
        try {

            String account = body.get("account");
            newSession.setAccount(account);
            newSession.setDeviceId(body.get("deviceId"));
            newSession.setHost(host);
            newSession.setChannel(body.get("channel"));
            newSession.setDeviceModel(body.get("device"));
            newSession.setClientVersion(body.get("appVersion"));
            newSession.setSystemVersion(body.get("osVersion"));
            newSession.setBindTime(System.currentTimeMillis());
            /*
             * 由于客户端断线服务端可能会无法获知的情况，客户端重连时，需要关闭旧的连接
             */
            IapImSessionDto oldSession = iapImImSessionService.get(account);

            /*
             * 如果是账号已经在另一台终端登录。则让另一个终端下线
             */

            if (oldSession != null && fromOtherDevice(newSession, oldSession) && oldSession.isConnected()) {
                sendForceOfflineMessage(oldSession, account, newSession.getDeviceModel());
            }

            /*
             * 有可能是同一个设备重复连接，则关闭旧的链接，这种情况一般是客户端断网，联网又重新链接上来，之前的旧链接没有来得及通过心跳机制关闭，在这里手动关闭
             * 条件1，连接来自是同一个设备
             * 条件2.2个连接都是同一台服务器
             */

            if (oldSession != null && !fromOtherDevice(newSession, oldSession) && Objects.equals(oldSession.getHost(), host)) {
                closeQuietly(oldSession);
            }

            iapImImSessionService.save(newSession);

        } catch (Exception e) {
            e.printStackTrace();
            reply.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            reply.setMessage(e.getMessage());
        }

        newSession.write(reply);
//        newSession.write(messageListDto());
    }

    private boolean fromOtherDevice(IapImSessionDto oldSession, IapImSessionDto newSession) {
        return !Objects.equals(oldSession.getDeviceId(), newSession.getDeviceId());
    }

    private void sendForceOfflineMessage(IapImSessionDto oldSession, String account, String deviceModel) {
        IapImMessageDto msg = new IapImMessageDto();
        msg.setAction(CIMConstant.MessageAction.ACTION_OFFLINE);
        msg.setReceiver(account);
        msg.setSender(Constants.SYSTEM);
        msg.setContent(deviceModel);
        MessageTemplate messageTemplate = new MessageTemplate();
        messageTemplate.putAll(msg);
        messageListTemplatePusher.setAllMessages(messageTemplate, account);
        closeQuietly(oldSession);
    }

    /**
     * 不同设备同一账号登录时关闭旧的连接
     */

    private void closeQuietly(IapImSessionDto oldSession) {
        if (oldSession.isConnected() && Objects.equals(host, oldSession.getHost())) {
            oldSession.setAttribute(CIMConstant.KEY_QUIETLY_CLOSE, true);
            oldSession.closeOnFlush();
        }
    }

}
