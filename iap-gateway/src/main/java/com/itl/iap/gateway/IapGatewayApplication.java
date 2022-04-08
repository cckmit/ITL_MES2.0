package com.itl.iap.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * TODO
 *
 * @author 汤俊
 * @date 2020-6-1 9:53
 * @since 1.0.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class IapGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(IapGatewayApplication.class, args);
    }
}