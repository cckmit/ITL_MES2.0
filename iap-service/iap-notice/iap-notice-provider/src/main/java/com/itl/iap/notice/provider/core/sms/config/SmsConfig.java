package com.itl.iap.notice.provider.core.sms.config;


import com.itl.iap.notice.provider.core.sms.service.ISendSmsService;
import com.itl.iap.notice.provider.core.sms.service.impl.SendSmsServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 短信平台自动配置类
 * @author 曾慧任
 * @date 2019/8/26
 * @since jdk1.8
 */
@Configuration
@EnableConfigurationProperties(AliyunProperties.class)
public class SmsConfig {
    @Autowired
    AliyunProperties aliyunProperties;

    @Bean
    @ConditionalOnClass({ SendSmsServiceImpl.class })
    @ConditionalOnProperty(name = "iap.sms.type", havingValue = "aliyun")
    public ISendSmsService smsSender() {
        SendSmsServiceImpl sender = new SendSmsServiceImpl();
        BeanUtils.copyProperties(aliyunProperties, sender);
        return sender;
    }
}
