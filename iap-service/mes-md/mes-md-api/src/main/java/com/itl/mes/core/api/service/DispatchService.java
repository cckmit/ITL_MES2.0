package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.dto.DeviceDto;
import com.itl.mes.core.api.dto.DispatchDTO;
import com.itl.mes.core.api.entity.Device;
import com.itl.mes.core.api.entity.Dispatch;

import java.util.List;
import java.util.Map;

public interface DispatchService extends IService<Dispatch> {
    List<Dispatch> okDispatchList(DispatchDTO dispatchDTO);
    Dispatch okDispatch(DispatchDTO dispatchDTO) throws CommonException;

    IPage<Device> pageDevice(DeviceDto deviceDto);
    List<Map<String,String>> selectBomDetail(String shopOrderBo);
}
