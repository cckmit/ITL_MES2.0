package iap.im.api.service;

import iap.im.api.dto.IapImMessageDto;
import iap.im.api.dto.IapImMessageListDto;

/**
 * 消息发送Service
 *
 * @author tanq
 * @date 2020-10-22
 * @since jdk1.8
 */
public interface IapImSendMessageService {

    /**
     * 发送消息
     *
     * @param iapImMessageDto
     * @return
     */
    IapImMessageDto sendMessage(IapImMessageDto iapImMessageDto);

    /**
     * 消息转发
     *
     * @param iapImMessageDto
     * @return
     */
    IapImMessageListDto forwardAllMessage(IapImMessageDto iapImMessageDto);
}
