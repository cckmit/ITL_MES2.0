package com.itl.iap.mes.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.mes.api.dto.DeviceInfoItemDto;
import com.itl.iap.mes.api.service.DeviceInfoItemService;
import com.itl.iap.mes.api.vo.DeviceInfoItemVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/deviceItem")
@Api("设备点检保养详细")
public class DeviceInfoItemController {
    @Autowired
    private DeviceInfoItemService deviceInfoItemService;

    @PostMapping("/query")
    public ResponseData<DeviceInfoItemVo> query(@RequestBody DeviceInfoItemDto deviceInfoItemDto) {
        return ResponseData.success(deviceInfoItemService.query(deviceInfoItemDto));
    }
}
