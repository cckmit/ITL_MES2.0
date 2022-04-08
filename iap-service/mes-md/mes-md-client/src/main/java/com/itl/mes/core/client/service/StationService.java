package com.itl.mes.core.client.service;

import com.itl.mes.core.client.service.impl.StationServiceImpl;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.vo.StationVo;
import com.itl.mes.core.client.config.FallBackConfig;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author yaoxiang
 * @date 2020/12/14
 * @since JDK1.8
 */
@FeignClient(value = "mes-md-provider",contextId = "station", fallback = StationServiceImpl.class, configuration = FallBackConfig.class)
public interface StationService {
    @GetMapping("/stations/query")
    @ApiOperation(value = "通过工位编号查询")
    ResponseData<StationVo> selectByStation(@RequestParam String station);


    @GetMapping("/stations/station/{station}")
    @ApiOperation(value = "通过工位查询数据")
    ResponseData<Map<String,Object>> getByStation(@PathVariable String station);
}
