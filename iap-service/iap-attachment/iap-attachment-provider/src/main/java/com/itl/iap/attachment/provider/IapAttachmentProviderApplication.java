package com.itl.iap.attachment.provider;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * TODO
 *
 * @author 汤俊
 * @date 2020-6-1 17:32
 * @since 1.0.0
 */
@EnableSwagger2
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.itl.iap.attachment.provider.mapper")
@EnableFeignClients(basePackages = {"com.itl.**"})
public class IapAttachmentProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(IapAttachmentProviderApplication.class, args);
    }


}
