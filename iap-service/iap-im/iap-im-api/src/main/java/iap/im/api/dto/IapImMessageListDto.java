package iap.im.api.dto;

import iap.im.api.entity.IapImGroupMember;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * DTO类-消息列表类（iap_im_message_user_t）
 *
 * @author 李骐光
 * @date 2020-10-10
 * @since jdk1.8
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
@NoArgsConstructor
public class IapImMessageListDto implements Serializable {

    /**
     * 主键ID
     */
    private String id;
    /**
     * 消息类型 0 用户 1 群
     */
    private int listType;
    /**
     * 发送人名称
     */
    private String sender;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 最后修改人
     */
    private String lastUpdateBy;

    /**
     * 最后修改时间
     */
    private Date lastUpdateDate;

    /**
     * 对方名称
     */
    private String receiver;
    /**
     * 对方真实名称
     */
    private String realName;
    /**
     * 对方头像
     */
    private String receiverAvatar;
    /**
     * 群名称
     */
    private String groupName;
    /**
     * 群头像
     */
    private String groupAvatar;
    /**
     * 未读消息数量
     */
    private long messageNumber;
    /**
     * 时间
     */
    private long timestamp;

    /**
     * 列表显示状态 0 显示 1 隐藏 2 置顶
     */
    private short showType;

    /**
     * 置顶时间
     */
    private long topTime;

    /**
     * 群成员数量
     */
    private Integer groupMemberNum;

    /**
     * 展示为0的数量（show_type = 0）
     */
    private Integer showTypeTrueNum;

    /**
     * 置顶消息
     */
    private List<IapImMessageTopDto> messageTopDto;

    /**
     * 最新消息集合
     */
    private List<IapImMessageDto> iapImMessageDtos;

    /**
     * 群组成员
     */
    private List<IapImGroupMember> groupMemberList;

    private String historyTime;

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("#Message#").append("\n");
        buffer.append("id:").append(id).append("\n");
        buffer.append("listType:").append(listType).append("\n");
        buffer.append("receiver:").append(receiver).append("\n");
        buffer.append("realName:").append(realName).append("\n");
        buffer.append("receiverAvatar:").append(receiverAvatar).append("\n");
        buffer.append("groupName:").append(groupName).append("\n");
        buffer.append("groupAvatar:").append(groupAvatar).append("\n");
        buffer.append("messageNumber:").append(messageNumber).append("\n");
        buffer.append("timestamp:").append(timestamp).append("\n");
        buffer.append("messageDtos:").append(iapImMessageDtos).append("\n");
        buffer.append("showType:").append(showType).append("\n");
        buffer.append("topTime").append(topTime).append("\n");
        buffer.append("groupMemberNum").append(groupMemberNum).append("\n");
        buffer.append("showTypeTrueNum").append(showTypeTrueNum).append("\n");
        return buffer.toString();
    }

}
