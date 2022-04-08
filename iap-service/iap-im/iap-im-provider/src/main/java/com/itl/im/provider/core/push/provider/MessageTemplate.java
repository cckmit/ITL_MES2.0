package com.itl.im.provider.core.push.provider;


import com.itl.iap.common.util.DtoUtils;
import iap.im.api.dto.IapImMessageDto;
import iap.im.api.dto.IapImMessageListDto;
import iap.im.api.dto.IapImMessageTopDto;
import iap.im.api.entity.IapImGroupMember;
import iap.im.api.entity.IapImMessageTop;
import iap.im.api.proto.MessageListDtoProto;
import iap.im.api.sendDto.Transportable;
import iap.im.api.variable.CIMConstant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tanq
 * @version 1.0
 * @date 2020/9/25
 * 消息推送模板
 */
public class MessageTemplate implements Serializable, Transportable {
    private List<Map<String, String>> listMap = new ArrayList<>();
    private Map<String, List<Map<String, String>>> messageList = new HashMap<>();

    private byte messageType;


    public MessageTemplate() {
        // 默认是发消息
        this.messageType = CIMConstant.ProtobufType.MESSAGE;
    }

    public MessageTemplate(byte messageType) {
        this.messageType = messageType;
    }

    public void put(String key, String value) {
        HashMap<String, String> map = new HashMap<>();
        map.put(key, value);
        listMap.add(map);
    }

    public void putAll(Object obj) {
        Map<String, String> maps = DtoUtils.objectToMap(obj);
        listMap.add(maps);
    }


    public void putListAll(List<IapImMessageListDto> objs) {
        objs.forEach(x -> {
            if (x.getIapImMessageDtos() != null && !x.getIapImMessageDtos().isEmpty()) {
                putMapList(x.getIapImMessageDtos(), x.getId());
            }
            if (x.getGroupMemberList() != null && !x.getGroupMemberList().isEmpty()) {
                putGroupMapList(x.getGroupMemberList(), x.getId() + "-group");
            }
            if (x.getGroupMemberList() != null && !x.getGroupMemberList().isEmpty()) {
                putTopMapList(x.getMessageTopDto(), x.getId() + "-top");
            }
            x.setIapImMessageDtos(null);
            x.setGroupMemberList(null);
            this.putAll(x);
        });
    }

    /**
     * 消息列表
     *
     * @param messages
     * @param id
     */
    private void putMapList(List<IapImMessageDto> messages, String id) {
        List<Map<String, String>> mapList = new ArrayList<>();
        messages.forEach(x -> {
            mapList.add(DtoUtils.objectToMap(x));
        });
        messageList.put(id, mapList);
    }

    /**
     * 群消息
     *
     * @param messages
     * @param id
     */
    private void putGroupMapList(List<IapImGroupMember> messages, String id) {
        List<Map<String, String>> mapList = new ArrayList<>();
        messages.forEach(x -> {
            mapList.add(DtoUtils.objectToMap(x));
        });
        messageList.put(id, mapList);
    }

    /**
     * 消息列表置顶
     *
     * @param messages
     * @param id
     */
    private void putTopMapList(List<IapImMessageTopDto> messages, String id) {
        List<Map<String, String>> mapList = new ArrayList<>();
        messages.forEach(x -> {
            mapList.add(DtoUtils.objectToMap(x));
        });
        messageList.put(id, mapList);
    }

    @Override
    public byte[] getBody() {
        MessageListDtoProto.ModelMessageList.Builder modeList = MessageListDtoProto.ModelMessageList.newBuilder();
        for (int i = 0; i < listMap.size(); i++) {
            Map<String, String> x = listMap.get(i);
            MessageListDtoProto.NewsMessage.Builder builder = MessageListDtoProto.NewsMessage.newBuilder();
            if (!messageList.isEmpty()) {
                List<Map<String, String>> mapList = messageList.get(x.get("id"));
                if (mapList != null && !mapList.isEmpty()) {
                    MessageListDtoProto.MessageList.Builder buildMessage = MessageListDtoProto.MessageList.newBuilder();
                    mapList.forEach(map -> {
                        MessageListDtoProto.MessageMap.Builder messageBuilder = MessageListDtoProto.MessageMap.newBuilder();
                        messageBuilder.putAllMessage(map);
                        buildMessage.addMessageMap(messageBuilder);
                    });
                    builder.putNewsMessage(x.get("id"), buildMessage.build());
                }
                List<Map<String, String>> groupList = messageList.get(x.get("id") + "-group");
                if (groupList != null && !groupList.isEmpty()) {
                    MessageListDtoProto.MessageList.Builder buildMessage = MessageListDtoProto.MessageList.newBuilder();
                    groupList.forEach(map -> {
                        MessageListDtoProto.MessageMap.Builder messageBuilder = MessageListDtoProto.MessageMap.newBuilder();
                        messageBuilder.putAllMessage(map);
                        buildMessage.addMessageMap(messageBuilder);
                    });
                    builder.putNewsMessage(x.get("id") + "-group", buildMessage.build());
                }
                List<Map<String, String>> topList = messageList.get(x.get("id") + "-top");
                if (topList != null && !topList.isEmpty()) {
                    MessageListDtoProto.MessageList.Builder buildMessage = MessageListDtoProto.MessageList.newBuilder();
                    topList.forEach(map -> {
                        MessageListDtoProto.MessageMap.Builder messageBuilder = MessageListDtoProto.MessageMap.newBuilder();
                        messageBuilder.putAllMessage(map);
                        buildMessage.addMessageMap(messageBuilder);
                    });
                    builder.putNewsMessage(x.get("id") + "-top", buildMessage.build());
                }
//                modeList.addMessage(builder);
            }
            builder.putAllStrMap(x);
            modeList.addMessage(builder);
        }
        return modeList.build().toByteArray();
    }


    @Override
    public byte getType() {
        return this.messageType;
    }


}
