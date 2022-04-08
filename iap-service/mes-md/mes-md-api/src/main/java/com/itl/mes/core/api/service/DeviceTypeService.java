package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.DeviceType;
import com.itl.mes.core.api.vo.DeviceTypeVo;
import com.itl.mes.core.api.vo.DeviceVo;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 设备类型表 服务类
 * </p>
 *
 * @author space
 * @since 2019-06-17
 */
public interface DeviceTypeService extends IService<DeviceType> {

    List<DeviceType> selectList();

    DeviceType selectByDeviceType(String deviceType) throws CommonException;

    void saveDeviceType(DeviceTypeVo deviceTypeVo) throws CommonException;

    DeviceTypeVo getDeviceTypeVoByDeviceType(String deviceType) throws CommonException;

   // void deleteDeviceType(String deviceType, Date modifyDate) throws CommonException;
    void deleteDeviceType(String deviceType) throws CommonException;
    List<DeviceVo> getDeviceVoList();
}