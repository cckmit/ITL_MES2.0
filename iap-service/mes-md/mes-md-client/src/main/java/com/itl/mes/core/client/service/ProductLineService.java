package com.itl.mes.core.client.service;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.vo.ProductLineVo;
import com.itl.mes.core.client.config.FallBackConfig;
import com.itl.mes.core.client.service.impl.ProductLineServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author yaoxiang
 * @date 2020/12/14
 * @since JDK1.8
 */
@FeignClient(value = "mes-md-provider",contextId = "productLine", fallback = ProductLineServiceImpl.class, configuration = FallBackConfig.class)
public interface ProductLineService {

    /**
     * 根据productLine查询
     *
     * @param productLine 产线
     * @return
     */
    @GetMapping("/productLines/query")
    @ApiOperation(value="通过产线查询数据")
    ResponseData<ProductLineVo> getProductLineByProductLine(String productLine );
}
