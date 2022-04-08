package com.itl.mes.core.provider.config;

import com.itl.mes.core.provider.webService.ItemWebService;
import com.itl.mes.core.provider.webService.ShopOrderWebService;
import com.itl.mes.core.provider.webService.SyncRouterWebService;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

@Configuration
public class WebServiceConfig {

    @Autowired
    private Bus bus;

    @Autowired
    private ItemWebService itemWebService;

    @Autowired
    ShopOrderWebService shopOrderWebService;

    @Autowired
    private SyncRouterWebService syncRouterWebService;

    @Bean
    public Endpoint endpointUserService() {
        EndpointImpl endpoint = new EndpointImpl(bus,itemWebService);
        endpoint.publish("/ERPToMESMaterialInfo");//接口发布在 /ItemWebService 目录下
        return endpoint;
    }

    @Bean
    public Endpoint endpointShopOrderService() {
        EndpointImpl endpoint = new EndpointImpl(bus,shopOrderWebService);
        endpoint.publish("/ERPToMESWoInfo");
        return endpoint;
    }

    @Bean
    public Endpoint endpointSyncRouterService() {
        EndpointImpl endpoint = new EndpointImpl(bus,syncRouterWebService);
        endpoint.publish("/ERPToMESRouterInfo");
        return endpoint;
    }
}
