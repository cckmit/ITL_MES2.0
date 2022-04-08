package com.itl.mes.core.client.config;

import com.itl.mes.core.client.service.impl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yaoxiang
 * @date 2020/12/14
 * @since JDK1.8
 */
@Configuration
public class FallBackConfig {

    @Bean
    public WorkShopServiceImpl workShopService() {
        return new WorkShopServiceImpl();
    }

    @Bean
    public ProductLineServiceImpl productLineService() {
        return new ProductLineServiceImpl();
    }

    @Bean
    public StationServiceImpl stationService() {
        return new StationServiceImpl();
    }

    @Bean
    public DeviceServiceImpl deviceService() {
        return new DeviceServiceImpl();
    }

    @Bean
    public CustomDataValServiceImpl customDataValService() {
        return new CustomDataValServiceImpl();
    }

    @Bean
    public NgCodeServiceFeignImpl ngCodeServiceFeign() {
        return new NgCodeServiceFeignImpl();
    }

    @Bean
    public RouterServiceImpl routerServiceFeign() {
        return new RouterServiceImpl();
    }
}
