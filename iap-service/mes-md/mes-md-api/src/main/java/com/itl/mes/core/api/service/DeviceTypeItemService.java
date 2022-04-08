package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.DeviceTypeItem;
import com.itl.mes.core.api.vo.DeviceSimplifyVo;
import com.itl.mes.core.api.vo.DeviceTypeSimplifyVo;

import java.util.List;

/**
 * <p>
 * 设备类型明细 服务类
 * </p>
 *
 * @author space
 * @since 2019-06-17
 */
public interface DeviceTypeItemService extends IService<DeviceTypeItem> {

    List<DeviceTypeItem> selectList();

    void save(String bo, List<DeviceTypeSimplifyVo> assignedDeviceTypeList) throws CommonException;

    void deleteByDeviceBO(String deviceBO)throws CommonException;

    void saveDeviceTypeItem(String bo, List<DeviceSimplifyVo> assignedDeviceList) throws CommonException;

    void deleteByDeviceTypeBO(String deviceTypeBO);
}