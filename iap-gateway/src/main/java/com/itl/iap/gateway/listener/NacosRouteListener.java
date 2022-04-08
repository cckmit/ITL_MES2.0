package com.itl.iap.gateway.listener;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.itl.iap.common.util.JsonUtils;
import com.itl.iap.gateway.config.NacosGatewayConfig;
import com.itl.iap.gateway.context.GatewayRouteEventPublisherAware;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;

/**
 * 实现runner，通过nacos下发动态路由配置
 *
 * @author 汤俊
 * @date 2020-6-15 20:10
 * @since jdk1.8
 */
@Slf4j
@Component
public class NacosRouteListener {

    @Autowired
    private NacosRouteRefreshListener nacosRouteRefreshListener;

    @Autowired
    private NacosGatewayConfig nacosGatewayConfig;

    @Autowired
    private GatewayRouteEventPublisherAware gatewayRouteEventPublisherAware;

    /**
     * 监听Nacos Server下发的动态路由配置
     */
    @PostConstruct
    public void loadRouteByNacosListener() {
        try {
            log.info("---->>> init nacos router data.");
            Properties nacosPro = new Properties();
            nacosPro.put("serverAddr", nacosGatewayConfig.getAddress());
            nacosPro.put("namespace", nacosGatewayConfig.getNameSpace());
            //添加命名空间
            ConfigService configService = NacosFactory.createConfigService(nacosPro);
            String configInfo = configService.getConfig(nacosGatewayConfig.getDataId(), nacosGatewayConfig.getGroupId(), nacosGatewayConfig.getTimeout());
            addRoute(configService.getConfig(nacosGatewayConfig.getDataId(), nacosGatewayConfig.getGroupId(), nacosGatewayConfig.getTimeout()));
            configService.addListener(nacosGatewayConfig.getDataId(), nacosGatewayConfig.getGroupId(), nacosRouteRefreshListener);
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加路由
     *
     * @param configInfo
     */
    private void addRoute(String configInfo) {
        if (StringUtils.isBlank(configInfo)) {
            throw new NullPointerException("route info is null");
        }
        List<RouteDefinition> list = JsonUtils.toList(configInfo, RouteDefinition.class);
        list.forEach(definition -> {
            gatewayRouteEventPublisherAware.update(definition);
        });
    }

}
