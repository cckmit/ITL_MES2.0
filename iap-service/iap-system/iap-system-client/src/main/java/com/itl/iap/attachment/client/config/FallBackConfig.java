package com.itl.iap.attachment.client.config;

import com.itl.iap.attachment.client.service.impl.OrganizationServiceImpl;
import com.itl.iap.attachment.client.service.impl.PositionServiceImpl;
import com.itl.iap.attachment.client.service.impl.RoleServiceImpl;
import com.itl.iap.attachment.client.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 远程调用配置
 *
 * @author 汤俊
 * @date 2020-6-30 15:04
 * @since jdk 1.8
 */
@Configuration
public class FallBackConfig {

    @Bean
    public UserServiceImpl userService() {
        return new UserServiceImpl();
    }

    @Bean
    public RoleServiceImpl roleService() {
        return new RoleServiceImpl();
    }

    @Bean
    public PositionServiceImpl positionService() {
        return new PositionServiceImpl();
    }

    @Bean
    public OrganizationServiceImpl organizationService() {
        return new OrganizationServiceImpl();
    }
}
