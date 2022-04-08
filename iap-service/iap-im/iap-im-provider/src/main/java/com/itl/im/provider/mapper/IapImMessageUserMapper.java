package com.itl.im.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import iap.im.api.dto.IapImMessageListDto;
import iap.im.api.entity.IapImMessageUser;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 消息列表Mapper
 *
 * @author mjl
 * @date 2020-10-10
 * @since jdk1.8
 */
public interface IapImMessageUserMapper extends BaseMapper<IapImMessageUser> {

    /**
     * 批量插入
     *
     * @param insertNewMessageUserList
     */
    Boolean insertList(@Param("list") List<IapImMessageUser> insertNewMessageUserList);

    /**
     * 通过群成员list和群ID，批量删除message_user中间表
     *
     * @param groupMemberAccountList
     * @param groupId
     */
    Boolean removeGroupMemberByGroupId(@Param("groupMemberAccountList") List<String> groupMemberAccountList, @Param("groupId") String groupId);

    /**
     * 通过sender和receiver双向删除
     *
     * @param sender
     * @param receiver
     */
    Boolean removeItemBySenderAndReceiver(@Param("sender") String sender, @Param("receiver") String receiver);

    /**
     * 根据群号，更新该群的所有展示信息
     *
     * @param groupId
     * @param showType
     */
    void updateShowTypeByGroupId(@Param("groupId") String groupId, @Param("showType") Short showType);

    /**
     * 批量更新群成员的消息列表显示状态
     *
     * @param groupId
     * @param groupMemberAccountList
     * @param showType
     */
    void updateList(@Param("groupId") String groupId, @Param("groupMemberAccountList") List<String> groupMemberAccountList, @Param("showType") Short showType);

}
