package com.itl.iap.notice.provider.core.email.exchanger;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.common.base.model.BaseModel;
import com.itl.iap.notice.api.entity.MsgMailConfiguration;
import com.itl.iap.notice.api.entity.MsgPublicTemplate;
import com.itl.iap.notice.api.entity.MsgType;
import com.itl.iap.notice.api.entity.email.model.EmailNotice;
import com.itl.iap.notice.api.enums.SendStatusEnum;
import com.itl.iap.notice.api.enums.SendTypeEnum;
import com.itl.iap.notice.provider.core.email.config.EmailConfig;
import com.itl.iap.notice.provider.core.email.server.ISendEmailService;
import com.itl.iap.notice.provider.core.email.vo.MailData;
import com.itl.iap.notice.provider.exchanger.AbstractNoticeExchanger;
import com.itl.iap.notice.provider.mapper.MsgMailConfigurationMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * 邮件通知适配器类
 *
 * @author 曾慧任
 * @date 2020-06-29
 * @since jdk1.8
 */
@Component
public class EmailAbstractNoticeExchanger extends AbstractNoticeExchanger {
    private Logger logger = LoggerFactory.getLogger(EmailAbstractNoticeExchanger.class);
    @Autowired
    private ISendEmailService sendEmailService;
    @Autowired
    private MsgMailConfigurationMapper msgMailConfigurationMapper;

    @Override
    public boolean  match(Map<String, Object> notice) {

        if (notice.get("type") == null || !String.valueOf(SendTypeEnum.EMAIL.getItem()).equals(notice.get("type").toString())) {
            return false;
        }
        return true;
    }

    @Override
    public boolean exchanger(Map<String, Object> map) throws Exception {
        EmailNotice notice = new EmailNotice();
        BeanUtils.populate(notice, map);
        boolean flag = false;
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
            msgMailConfiguration.setEnable(Integer.valueOf(BaseModel.ENABLED));
            msgMailConfiguration.setType(MsgMailConfiguration.MAIL);
            QueryWrapper<MsgMailConfiguration> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("enable", Integer.valueOf(BaseModel.ENABLED)).eq("type", MsgMailConfiguration.MAIL);
            msgMailConfiguration = msgMailConfigurationMapper.selectOne(queryWrapper);
            if (msgMailConfiguration == null) {
                throw new RuntimeException("未找到启动的配置！");
            }
            EmailConfig emailConfig = new EmailConfig();
            emailConfig.setUsername(msgMailConfiguration.getUsername());
            emailConfig.setPassword(msgMailConfiguration.getPassword());
            emailConfig.setMailServerHost(msgMailConfiguration.getIp());
            emailConfig.setMailServerPort(msgMailConfiguration.getPort());
            emailConfig.setProtocol(msgMailConfiguration.getProtocol());
            emailConfig.setFromAddress(msgMailConfiguration.getUsername());

            MailData mailData = new MailData();
            mailData.setSubject(title);
            mailData.setContent(sysNoticeContent);
            mailData.setToAddresss(notice.getToAddress());
            mailData.setCcAddresss(notice.getCcAddress());
            //发送邮件
            sendEmailService.sendMail(mailData, emailConfig);
            //发送记录
            saveSendMessage(msgType, msgPublicTemplate, title, sysNoticeContent, SendTypeEnum.SMS.getItem(), SendStatusEnum.SEND_SUCCESS.getItem());
            flag = true;
            logger.info("send success!");
            return flag;
        } catch (Exception e) {
            e.getStackTrace();
            saveSendMessage(msgType, msgPublicTemplate, title, sysNoticeContent,
                    SendTypeEnum.EMAIL.getItem(), SendStatusEnum.SEND_FAIL.getItem());
            return false;
        }
    }

//    public List<String> getSplitAddress(String address) {
//        return Arrays.asList(address.split(","));
//    }

}
