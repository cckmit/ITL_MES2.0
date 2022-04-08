package com.itl.iap.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * TODO
 *
 * @author 汤俊
 * @date 2020-6-18 23:34
 * @since 1.0.0
 */
@EnableDiscoveryClient
@MapperScan("com.itl.iap.auth.mapper")
@EnableFeignClients(basePackages = {"com.itl.**"})
@SpringBootApplication
public class IapAuthProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(IapAuthProviderApplication.class, args);
    }

}
