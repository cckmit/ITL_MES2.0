package com.xxl.job.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author xuxueli 2018-10-28 00:38:13
 */
@EnableDiscoveryClient
@EnableSwagger2
@SpringBootApplication
@MapperScan("com.xxl.job.admin.mapper")
public class IapXxlJobAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(IapXxlJobAdminApplication.class, args);
    }

}