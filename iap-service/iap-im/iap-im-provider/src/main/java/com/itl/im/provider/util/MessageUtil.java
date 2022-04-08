package com.itl.im.provider.util;

import com.itl.iap.common.util.DtoUtils;
import iap.im.api.dto.IapImMessageSettingDto;
import iap.im.api.entity.IapImMessage;
import iap.im.api.entity.IapImMessageSetting;
import iap.im.api.entity.IapImMessageUser;
import iap.im.api.variable.Constants;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 消息工具类
 *
 * @author tanq
 * @date 2020-10-21
 * @since jdk1.8
 */
public class MessageUtil {

    /**
     * 根据传入的IM用户信息，交换用户名称后返回一个新的用户
     *
     * @param iapImMessageUser
     * @return
     */
    public static IapImMessageUser convertUser(IapImMessageUser iapImMessageUser) {
        IapImMessageUser convertOneUser = DtoUtils.convertObj(iapImMessageUser, IapImMessageUser.class);
        convertOneUser.setSender(iapImMessageUser.getReceiver());
        convertOneUser.setReceiver(iapImMessageUser.getSender());
        convertOneUser.setId(SnowIdUtil.getLongId());
        return convertOneUser;
    }


    /**
     * 自动回复时间类型判断
     *
     * @param messageSetting
     * @param messages
     * @return true 直接跳出 false  发送信息
     */
    public static boolean autoReplyTime(IapImMessageSettingDto messageSetting, List<IapImMessage> messages) {
        if (messageSetting.getTimestamp() == null || messageSetting.getTimeType() == null) {
            return true;
        }
        Calendar now = Calendar.getInstance();
        switch (messageSetting.getTimeType()) {
            //  秒
            case Constants.MessageSetting.TIME_TYPE0:
                now.add(Calendar.SECOND, -messageSetting.getTimeType());
                break;
            // 分
            case Constants.MessageSetting.TIME_TYPE1:
                now.add(Calendar.MINUTE, -messageSetting.getTimeType());
                break;
            // 时
            case Constants.MessageSetting.TIME_TYPE2:
                now.add(Calendar.HOUR, -messageSetting.getTimeType());
                break;
            default:
                return true;
        }
        List<IapImMessage> collect = messages.stream().filter(x -> now.getTimeInMillis() <= x.getTimestamp()).collect(Collectors.toList());
        // 如果当前发送频率小于或等于配置的频率  则发送
        if (collect.size() <= messageSetting.getFrequency()) {
            return false;
        }
        return true;
    }

}
