package com.itl.im.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.common.base.dto.UserTDto;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.util.DtoUtils;
import com.itl.im.provider.core.push.MessageListTemplatePusher;
import com.itl.im.provider.mapper.IapImGroupMemberMapper;
import com.itl.im.provider.mapper.IapImMessageMapper;
import com.itl.im.provider.util.SnowIdUtil;
import com.itl.im.provider.util.ThreadPoolExpansion;
import iap.im.api.dto.IapImMessageDto;
import iap.im.api.dto.IapImMessageListDto;
import iap.im.api.entity.IapImGroupMember;
import iap.im.api.entity.IapImMessage;
import iap.im.api.service.IapImGroupMessageService;
import iap.im.api.service.IapImMessageService;
import iap.im.api.service.IapImSendMessageService;
import iap.im.api.variable.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 消息发送Service
 *
 * @author tanq
 * @date 2020/10/22
 * @since jdk1.8
 */
@Service
public class IapImSendMessageServiceImpl implements IapImSendMessageService {
    @Autowired
    private IapImMessageService iapImMessageService;
    @Autowired
    private IapImGroupMessageService iapImGroupMessageService;
    @Autowired
    private UserUtil userUtil;

    /**
     * 发送消息
     *
     * @param iapImMessageDto
     * @return
     */
    @Override
    public IapImMessageDto sendMessage(IapImMessageDto iapImMessageDto) {
        UserTDto user = userUtil.getUser();
        iapImMessageDto.setSender(user.getUserName());
        iapImMessageDto.setId(SnowIdUtil.getLongId());
        iapImMessageDto.setTimestamp(System.currentTimeMillis());

        if (iapImMessageDto.getAction().equals(Constants.Message.ACTION_0)) {
            // 保存消息到数据库
            iapImMessageService.saveAllMessage(DtoUtils.convertObj(iapImMessageDto, IapImMessage.class));
            // 过滤敏感词
            iapImMessageDto = iapImMessageService.getDefaultFilter(iapImMessageDto);
            return iapImMessageService.sendMessage(iapImMessageDto);
        } else {
            return iapImGroupMessageService.sendMessage(iapImMessageDto);
        }
    }

    /**
     * 消息转发
     *
     * @param iapImMessageDto
     * @return
     */
    @Override
    public IapImMessageListDto forwardAllMessage(IapImMessageDto iapImMessageDto) {
        UserTDto user = userUtil.getUser();
        iapImMessageDto.setSender(user.getUserName());
        iapImMessageDto.setId(SnowIdUtil.getLongId());
        iapImMessageDto.setTimestamp(System.currentTimeMillis());
        // 保存消息到数据库
        iapImMessageService.saveAllMessage(DtoUtils.convertObj(iapImMessageDto, IapImMessage.class));
        iapImMessageDto = iapImMessageService.getDefaultFilter(iapImMessageDto);
        if (iapImMessageDto.getAction().equals(Constants.Message.ACTION_0)) {
            return iapImMessageService.forwardMessage(iapImMessageDto);
        } else {
            iapImGroupMessageService.sendMessage(iapImMessageDto);
        }
        return null;
    }

}
