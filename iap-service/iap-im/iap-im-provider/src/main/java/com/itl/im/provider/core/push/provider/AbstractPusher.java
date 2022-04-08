package com.itl.im.provider.core.push.provider;

import com.itl.im.provider.feign.IRemoteMessageServiceProxy;
import iap.im.api.dto.IapImMessageDto;
import iap.im.api.sendDto.IapImSessionDto;
import iap.im.api.service.IapImApnsService;
import iap.im.api.service.IapImIMSessionService;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author tanq
 * @version 1.0
 * @date 2020/9/26
 */
public abstract class AbstractPusher {
    @Value("#{'${server.host}'.trim()}")
    private String host;

    @Resource
    private IapImApnsService iapImApnsService;

    @Resource
    private IRemoteMessageServiceProxy remoteMessageServiceProxy;

    @Resource
    protected IapImIMSessionService iapImImSessionService;


    public final void push(IapImMessageDto message, IapImSessionDto session) {

        if (session == null) {
            return;
        }

        /*
         * IOS设备，如果开启了apns，则使用apns推送
         */
        if (session.isIOSChannel() && session.isApnsEnable()) {
            iapImApnsService.push(message, session.getDeviceId());
            return;
        }

        /*
         * 服务器集群时，判断当前session是否连接于本台服务器
         * 如果连接到了其他服务器则转发请求到目标服务器
         */
        if (session.isConnected() && !Objects.equals(host, session.getHost())) {
            remoteMessageServiceProxy.forward(message, session.getHost());
            return;
        }

        /*
         * 如果是Android，浏览器或者windows客户端则直接发送
         */
        if (session.isConnected() && Objects.equals(host, session.getHost())) {
//            session.write(message);
        }
    }
}
