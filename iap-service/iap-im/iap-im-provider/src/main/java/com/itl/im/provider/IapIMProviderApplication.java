package com.itl.im.provider;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author tanq
 * @version 1.0
 * @date 2020/9/25
 */
@EnableAsync
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.itl.**"})
@SpringBootApplication
@MapperScan("com.itl.im.provider.mapper")
public class IapIMProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(IapIMProviderApplication.class, args);
    }
}
