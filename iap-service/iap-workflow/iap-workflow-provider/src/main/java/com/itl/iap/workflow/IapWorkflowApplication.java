package com.itl.iap.workflow;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * TODO
 *
 * @author 汤俊
 * @date 2020-5-29 11:03
 * @since 1.0.0
 */
@SpringBootApplication
@EnableDiscoveryClient
@RestController
//@RefreshScope
@EnableSwagger2
@EnableFeignClients(basePackages = {"com.itl.**"})
@EnableProcessApplication
public class IapWorkflowApplication {

    public static void main(String[] args) {
        SpringApplication.run(IapWorkflowApplication.class, args);
    }

}
