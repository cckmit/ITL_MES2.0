package com.itl.mes.core.client.service.impl;


import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.entity.Router;
import com.itl.mes.core.client.service.RouterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * @author linjl
 * @date 2021/2/4
 */
@Slf4j
@Component
public class RouterServiceImpl implements RouterService {

    @Override
    public ResponseData<Router> getRouter(String router) {
        log.error("sorry RouterService getRouter feign fallback, router:{}" + router);
        return null;
    }
}
