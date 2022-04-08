package com.itl.mes.core.client.service;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.entity.Router;
import com.itl.mes.core.client.config.FallBackConfig;
import com.itl.mes.core.client.service.impl.RouterServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author linjl
 * @date 2021/2/4
 */
@FeignClient(value = "mes-md-provider",contextId = "router", fallback = RouterServiceImpl.class, configuration = FallBackConfig.class)
public interface RouterService {
    /**
     * 获取工艺路线信息
     * */
    @GetMapping("/router/query")
    @ApiOperation(value = "获取工艺路线信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "router", value = "router", dataType = "string", paramType = "query")
    })
    ResponseData<Router> getRouter(@RequestParam("router") String router);

}
