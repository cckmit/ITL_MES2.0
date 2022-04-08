package com.itl.iap.notice.provider.core.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云配置
 *
 * @author 曾慧任
 * @date 2019/8/26
 * @since jdk1.8
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "iap.sms.aliyun")
public class AliyunProperties {
    private String accessKeyId;
    private String accessKeySecret;
}
