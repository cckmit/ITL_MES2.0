package com.itl.iap.attachment.client.config;

import com.itl.iap.attachment.client.service.impl.IapImUploadFileServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO
 *
 * @author 汤俊
 * @date 2020-6-30 15:04
 * @since 1.0.0
 */
@Configuration
public class AttachmentFallBackConfig {

    @Bean
    public IapImUploadFileServiceImpl iapImUploadFileService() {
        return new IapImUploadFileServiceImpl();
    }

}
