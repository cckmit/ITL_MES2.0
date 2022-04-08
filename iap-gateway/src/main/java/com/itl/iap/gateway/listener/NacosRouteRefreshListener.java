package com.itl.iap.gateway.listener;

import com.alibaba.nacos.api.config.listener.Listener;
import com.itl.iap.common.util.JsonUtils;
import com.itl.iap.gateway.context.GatewayRouteEventPublisherAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * TODO
 * Nacos Route 刷新监听器
 *
 * @author 汤俊
 * @date 2020-6-16 2:10
 * @since jdk1.8
 */
@Component
public class NacosRouteRefreshListener implements Listener {

    @Autowired
    private GatewayRouteEventPublisherAware gatewayRouteEventPublisherAware;

    public NacosRouteRefreshListener() {
        System.out.println("--->>> Init NacosRouteRefreshListener.");
    }

    @Override
    public Executor getExecutor() {
        return null;
    }

    @Override
    public void receiveConfigInfo(String configInfo) {
        List<RouteDefinition> list = JsonUtils.toList(configInfo, RouteDefinition.class);
        list.forEach(definition -> {
            gatewayRouteEventPublisherAware.update(definition);
        });
    }
}
