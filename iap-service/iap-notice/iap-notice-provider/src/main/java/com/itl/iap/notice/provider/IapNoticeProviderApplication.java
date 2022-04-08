package com.itl.iap.notice.provider;

import com.itl.iap.notice.provider.core.websocket.WebSocket;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author tanq
 * @version 1.0
 * @date 2020/10/27
 * @JDK 1.8
 * @description 消息中心启动类
 */
@EnableSwagger2
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.itl.iap.notice.provider.mapper")
@EnableFeignClients(basePackages = {"com.itl.**"})
public class IapNoticeProviderApplication {
    public static void main(String[] args) {
        //SpringApplication.run(IapNoticeProviderApplication.class, args);
        ConfigurableApplicationContext applicationContext=SpringApplication.run(IapNoticeProviderApplication.class, args);
        WebSocket.setApplicationContext(applicationContext);
        System.out.println("启动成功");
    }
}
