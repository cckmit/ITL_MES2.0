package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.DeviceDto;
import com.itl.mes.core.api.entity.Device;
import com.itl.mes.core.api.vo.DeviceTypeVo;
import com.itl.mes.core.api.vo.DeviceVo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备表 服务类
 * </p>
 *
 * @author space
 * @since 2019-06-17
 */
public interface DeviceService extends IService<Device> {

    List<Device> selectList();

    void saveDevice(DeviceVo deviceVo) throws CommonException;

    DeviceVo getDeviceVoByDevice(String device) throws CommonException;

   //void deleteDevice(String device, Date modifyDate) throws CommonException;
   void deleteDevice(String device) throws CommonException;
    List<DeviceTypeVo> getDeviceTypeVoList();
    IPage<Device> selectDeviceWorkshop(DeviceDto deviceDto);

    IPage getScrewByLine(Map<String, Object> params);

    IPage<Device> query(DeviceDto deviceDto);

    DeviceVo getDeviceVoTypeByDevice(String device) throws CommonException;

    Device getDeviceById(String id);
}