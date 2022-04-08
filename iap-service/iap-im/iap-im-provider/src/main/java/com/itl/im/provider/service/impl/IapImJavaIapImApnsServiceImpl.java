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
package com.itl.im.provider.service.impl;

import cn.teaey.apns4j.Apns4j;
import cn.teaey.apns4j.network.ApnsChannel;
import cn.teaey.apns4j.network.ApnsChannelFactory;
import cn.teaey.apns4j.network.ApnsGateway;
import cn.teaey.apns4j.protocol.ApnsPayload;
import iap.im.api.dto.IapImMessageDto;
import iap.im.api.service.IapImApnsService;
import iap.im.api.variable.Constants;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 * 消息推送Service实现类
 *
 * @author tanq
 * @date 2020-10-10
 * @since jdk1.8
 */
@Service
public class IapImJavaIapImApnsServiceImpl implements IapImApnsService {
    private final Logger LOGGER = LoggerFactory.getLogger(IapImJavaIapImApnsServiceImpl.class);
    //	@Value("${apple.apns.p12.password}")
    private String password;
    //	@Value("${apple.apns.p12.file}")
    private String p12Path;
    //	@Value("${apple.apns.debug}")
    private boolean isDebug;

    /**
     * 推送消息
     *
     * @param message     消息
     * @param deviceToken 接收者Token
     */
    @Override
    public void push(IapImMessageDto message, String deviceToken) {

        if (StringUtils.isBlank(deviceToken)) {
            return;
        }
        // 只提示聊天相关消息
        if (!message.getAction().equals(Constants.MessageAction.ACTION_0)
                && message.getAction().equals(Constants.MessageAction.ACTION_2)
                && message.getAction().equals(Constants.MessageAction.ACTION_3)
                && message.getAction().equals(Constants.MessageAction.ACTION_201)) {
            return;
        }

        InputStream stream = getClass().getResourceAsStream(p12Path);
        ApnsChannelFactory apnsChannelFactory = Apns4j.newChannelFactoryBuilder()
                .keyStoreMeta(stream)
                .keyStorePwd(password)
                .apnsGateway(isDebug ? ApnsGateway.DEVELOPMENT : ApnsGateway.PRODUCTION)
                .build();

        ApnsChannel apnsChannel = apnsChannelFactory.newChannel();

        try {

            ApnsPayload apnsPayload = new ApnsPayload();
            apnsPayload.extend("id", message.getId());
            apnsPayload.extend("action", message.getAction());
            apnsPayload.extend("content", message.getContent());
            apnsPayload.extend("sender", message.getSender());
            apnsPayload.extend("receiver", message.getReceiver());
            apnsPayload.extend("format", message.getFormat());
            apnsPayload.extend("extra", message.getExtra());
            apnsPayload.extend("timestamp", message.getTimestamp());

            apnsChannel.send(deviceToken, apnsPayload);

            LOGGER.info("Apns push done.\ndeviceToken : {} \napnsPayload : {}", deviceToken, apnsPayload.toJsonString());
        } catch (Exception e) {
            LOGGER.error("Apns push failed", e);
        } finally {
            IOUtils.closeQuietly(stream);
            IOUtils.closeQuietly(apnsChannel);
        }
    }
}
