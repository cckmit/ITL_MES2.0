package iap.im.api.service;

import iap.im.api.dto.IapImGroupMemberDto;
import iap.im.api.dto.IapImMessageDto;
import iap.im.api.dto.IapImMessageListDto;
import iap.im.api.entity.IapImGroupMember;
import iap.im.api.entity.IapImMessage;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 群消息Service层
 *
 * @author tanq
 * @date 2020-10-20
 * @since jdk1.8
 */
public interface IapImGroupMessageService {

    /**
     * 群消息发送
     *
     * @param iapImMessageDto 当前消息
     * @return
     */
    IapImMessageDto sendMessage(IapImMessageDto iapImMessageDto);

    /**
     * 创建群聊
     *
     * @param groupMemberList 群成员列表
     * @return
     */
    IapImMessageListDto createGroup(List<IapImGroupMemberDto> groupMemberList);

    /**
     * 添加群成员
     *
     * @param groupId    群ID
     * @param memberList 需要添加的群成员列表
     */
    void addGroupMember(String groupId, List<IapImGroupMemberDto> memberList);

    /**
     * 保存群消息
     *
     * @param oldIapImessageDto 群成员主动发送的消息
     * @return
     */
    List<IapImMessage> saveGroupMessages(IapImMessageDto oldIapImessageDto);

}
