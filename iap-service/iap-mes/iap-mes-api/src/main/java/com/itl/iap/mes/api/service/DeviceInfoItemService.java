package com.itl.iap.mes.api.service;

import com.itl.iap.mes.api.dto.DeviceInfoItemDto;
import com.itl.iap.mes.api.vo.DeviceInfoItemVo;

import java.util.Map;

public interface DeviceInfoItemService {
    DeviceInfoItemVo query(DeviceInfoItemDto param);
}
