package com.itl.mes.core.client.service;

import com.itl.mes.core.client.service.impl.WorkShopServiceImpl;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.entity.WorkShop;
import com.itl.mes.core.api.vo.WorkShopVo;
import com.itl.mes.core.client.config.FallBackConfig;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author yaoxiang
 * @date 2020/12/14
 * @since JDK1.8
 */
@FeignClient(value = "mes-md-provider",contextId = "workshop", fallback = WorkShopServiceImpl.class, configuration = FallBackConfig.class)
public interface WorkShopService {
    /**
     * 根据workShop查询
     *
     * @param workShop 车间
     * @return
     */
    @GetMapping("/workShops/query")
    @ApiOperation(value="通过车间查询数据")
    ResponseData<WorkShopVo> getWorkShopByWorkShop(String workShop);

    /**
     * 通过工厂查询数据
     *
     * @return
     */
    @GetMapping("/workShops/getWorkShopBySite")
    @ApiOperation(value="通过车间查询数据")
    List<WorkShop> getWorkShopBySite();

}
