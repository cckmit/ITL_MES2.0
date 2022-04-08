package iap.im.api.service;

import iap.im.api.entity.IapImMessage;
import iap.im.api.entity.IapImMessageUser;

import java.util.List;

/**
 * 消息列表Service层
 *
 * @author mjl
 * @date 2020-10-10
 * @since jdk1.8
 */
public interface IapImMessageUserService {

    /**
     * 通过 sender和 receiver 双向删除
     *
     * @param sender
     * @param receiver
     * @return
     */
    Boolean removeDoubleMessageUserItem(String sender, String receiver, Short listType);

    /**
     * 批量双向添加群成员到 iap_im_message_user_t 表
     *
     * @param groupMemberList
     * @param groupId
     * @return
     */
    Boolean addGroupMessageUserItem(List<String> groupMemberList, String groupId);

    /**
     * 批量双向删除群成员到 iap_im_message_user_t 表
     *
     * @param groupMemberList
     * @param groupId
     * @return
     */
    Boolean removeGroupMessageUserItem(List<String> groupMemberList, String groupId);

    /**
     * 通过传入的消息判断这个子项在不在 iap_im_message_user_t 表，如果不存在，则新增该子项
     *
     * @param iapImMessage
     */
    void saveOrUpdateMessageUserItem(IapImMessage iapImMessage);

    /**
     * 更新
     *
     * @param iapImMessageUser
     * @return
     */
    Boolean updateIapImMessageUser(IapImMessageUser iapImMessageUser);

}
