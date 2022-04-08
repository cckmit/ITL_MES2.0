package com.itl.mes.core.client.service;

import com.itl.mes.core.client.service.impl.DeviceServiceImpl;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.vo.DeviceVo;
import com.itl.mes.core.client.config.FallBackConfig;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author yaoxiang
 * @date 2020/12/14
 * @since JDK1.8
 */
@FeignClient(value = "mes-md-provider", contextId = "device", fallback = DeviceServiceImpl.class, configuration = FallBackConfig.class)
public interface DeviceService {
    /**
     * 根据device查询
     *
     * @param device 设备编号
     * @return
     */
    @GetMapping("/devices/query")
    @ApiOperation(value="通过设备编号查询数据")
    ResponseData<DeviceVo> getDeviceVoByDevice(String device );
}
