package com.itl.im.provider.core.push;

import com.itl.im.provider.core.push.provider.MessageTemplate;
import iap.im.api.dto.IapImMessageListDto;
import iap.im.api.sendDto.IapImSessionDto;
import iap.im.api.service.IapImIMSessionService;
import iap.im.api.variable.CIMConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author tanq
 * @version 1.0
 * @date 2020/9/25
 * 消息列表推送模板
 */
@Component
public class MessageListTemplatePusher {
    @Autowired
    private IapImIMSessionService iapImImSessionService;
    @Value("#{'${server.host}'.trim()}")
    private String host;

    /**
     * 推送消息集合给指定用户
     *
     * @param listMessage 发送消息内容
     * @param user        接收用户
     */
    public boolean sendMessage(List<IapImMessageListDto> listMessage, String user) {
        MessageTemplate messageTemplate = new MessageTemplate(CIMConstant.ProtobufType.MESSAGE_LIST);
        messageTemplate.putListAll(listMessage);
        return setAllMessages(messageTemplate, user);
    }

    /**
     * 消息推送
     *
     * @param messages 消息模板
     * @param receiver 接收人
     */
    public boolean setAllMessages(MessageTemplate messages, String receiver) {
        IapImSessionDto iapImSessionDto = iapImImSessionService.get(receiver);
        if (iapImSessionDto == null || iapImSessionDto.getSession() == null) {
            return false;
        }
        this.push(messages, iapImSessionDto);
        return true;
    }

    /**
     * 向用户发送消息
     *
     * @param message
     */
    private final void push(MessageTemplate message, IapImSessionDto session) {
        if (session == null) {
            return;
        }
        /*
         * 如果是Android，浏览器或者windows客户端则直接发送
         */
        if (session.isConnected() && Objects.equals(host, session.getHost())) {
            session.write(message);
        }
    }
}
