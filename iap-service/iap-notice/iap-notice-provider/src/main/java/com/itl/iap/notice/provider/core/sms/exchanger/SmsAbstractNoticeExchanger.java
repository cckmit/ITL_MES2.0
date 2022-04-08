package com.itl.iap.notice.provider.core.sms.exchanger;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.notice.api.entity.MsgMailConfiguration;
import com.itl.iap.notice.api.entity.MsgPublicTemplate;
import com.itl.iap.notice.api.entity.MsgType;
import com.itl.iap.notice.api.entity.email.model.SmsNotice;
import com.itl.iap.notice.api.enums.SendStatusEnum;
import com.itl.iap.notice.api.enums.SendTypeEnum;
import com.itl.iap.notice.provider.core.sms.service.ISendSmsService;
import com.itl.iap.notice.provider.exchanger.AbstractNoticeExchanger;
import com.itl.iap.notice.provider.mapper.MsgMailConfigurationMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 短信通知适配器类
 *
 * @author 曾慧任
 * @date 2020-06-29
 * @since jdk1.8
 */
@Component
public class SmsAbstractNoticeExchanger extends AbstractNoticeExchanger {
    @Autowired
    private ISendSmsService sendSmsService;

    @Autowired
    private MsgMailConfigurationMapper msgMailConfigurationMapper;


    @Override
    public boolean match(Map<String, Object> notice) {
        if (notice.get("type") == null || !String.valueOf(SendTypeEnum.SMS.getItem()).equals(notice.get("type").toString())) {
            return false;
        }
        return true;
    }

    @Override
    public boolean exchanger(Map<String, Object> map) {
        SmsNotice notice = new SmsNotice();
        try {
            BeanUtils.populate(notice, map);
        } catch (Exception e) {
            throw new RuntimeException("map转对象失败");
        }

        String code = notice.getCode();
        Map<String, Object> params = notice.getParams();
        Map<String, Object> result = parseMes(code, params);
        MsgType msgType = (MsgType) result.get("msgType");
        MsgPublicTemplate msgPublicTemplate = (MsgPublicTemplate) result.get("msgPublicTemplate");
        String title = result.get("title") == null ? "" : result.get("title").toString();
        String sysNoticeContent = result.get("sysNoticeContent") == null ? "" : result.get("sysNoticeContent").toString();

        msgPublicTemplate.setUserId(notice.getUserId())
                .setUserName(notice.getUserName())
                .setBusinessId(notice.getBusinessId());

        try {
            MsgMailConfiguration msgMailConfiguration = new MsgMailConfiguration();
            msgMailConfiguration.setEnable(MsgMailConfiguration.ENABLE);
            msgMailConfiguration.setType(MsgMailConfiguration.SMS);
            QueryWrapper<MsgMailConfiguration> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("enable", MsgMailConfiguration.ENABLE).eq("type", MsgMailConfiguration.SMS);
            msgMailConfiguration = msgMailConfigurationMapper.selectOne(queryWrapper);
            if (msgMailConfiguration == null) {
                throw new RuntimeException("发送渠道为空");
            }
            //发送短信
            sendSmsService.sendSms(notice, msgPublicTemplate, msgMailConfiguration);
            //记录
            saveSendMessage(msgType, msgPublicTemplate, title, sysNoticeContent, SendTypeEnum.SMS.getItem(), SendStatusEnum.SEND_SUCCESS.getItem());
            return true;
        } catch (Exception e) {
            saveSendMessage(msgType, msgPublicTemplate, title, sysNoticeContent, SendTypeEnum.SMS.getItem(), SendStatusEnum.SEND_FAIL.getItem());
            e.printStackTrace();
            throw new RuntimeException("map转对象失败");
        }
    }
}
