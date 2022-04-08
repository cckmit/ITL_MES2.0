package com.itl.mes.core.client.service.impl;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.vo.DeviceVo;
import com.itl.mes.core.client.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author yaoxiang
 * @date 2020/12/14
 * @since JDK1.8
 */
@Slf4j
@Component
public class DeviceServiceImpl implements DeviceService {
    @Override
    public ResponseData<DeviceVo> getDeviceVoByDevice(String device) {
        log.error("sorry DeviceService getDeviceVoByDevice feign fallback device:{}" + device);
        return null;
    }
}
