package com.itl.mes.core.client.service;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.entity.NcCode;
import com.itl.mes.core.client.config.FallBackConfig;

import com.itl.mes.core.client.service.impl.NgCodeServiceFeignImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * liKun
 */
@FeignClient(value = "mes-md-provider", fallback = NgCodeServiceFeignImpl.class, configuration = FallBackConfig.class)
public interface NgCodeServiceFeign {

    /**
     * 参数:List<BO> 目的:NcCode表 查询NcCode
     */
    @PostMapping("/ncCodes/selectListNcCode/byListBo")
    ResponseData<List<NcCode>> selectByListBo(@RequestBody List<String> boList);

}
