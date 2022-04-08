package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.Device;
import com.itl.mes.core.api.entity.DeviceType;
import com.itl.mes.core.api.entity.DeviceTypeItem;
import com.itl.mes.core.api.service.DeviceTypeItemService;
import com.itl.mes.core.api.vo.DeviceSimplifyVo;
import com.itl.mes.core.api.vo.DeviceTypeSimplifyVo;
import com.itl.mes.core.provider.mapper.DeviceTypeItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 设备类型明细 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-06-17
 */
@Service
@Transactional
public class DeviceTypeItemServiceImpl extends ServiceImpl<DeviceTypeItemMapper, DeviceTypeItem> implements DeviceTypeItemService {


    @Autowired
    private DeviceTypeItemMapper deviceTypeItemMapper;

    @Autowired
    private DeviceTypeServiceImpl deviceTypeService;

    @Autowired
    private DeviceServiceImpl deviceService;

    @Override
    public List<DeviceTypeItem> selectList() {
        QueryWrapper<DeviceTypeItem> entityWrapper = new QueryWrapper<>();
        //getEntityWrapper(entityWrapper, deviceTypeItem);
        return super.list(entityWrapper);
    }


    @Override
    public void save(String deviceBO, List<DeviceTypeSimplifyVo> assignedDeviceTypeList) throws CommonException {
        if(assignedDeviceTypeList != null) {
            deleteByDeviceBO(deviceBO);
            for (DeviceTypeSimplifyVo deviceTypeSimplifyVo : assignedDeviceTypeList) {
                DeviceType deviceType = deviceTypeService.selectByDeviceType(deviceTypeSimplifyVo.getDeviceType());
                DeviceTypeItem deviceTypeItem = new DeviceTypeItem();
                deviceTypeItem.setDeviceBo(deviceBO);
                deviceTypeItem.setDeviceTypeBo(deviceType.getBo());
                deviceTypeItemMapper.insert(deviceTypeItem);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void deleteByDeviceBO(String deviceBO) {
        QueryWrapper<DeviceTypeItem> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(DeviceTypeItem.DEVICE_BO,deviceBO);
        deviceTypeItemMapper.delete(entityWrapper);
    }

    @Override
    public void saveDeviceTypeItem(String deviceTypeBO, List<DeviceSimplifyVo> assignedDeviceList) throws CommonException {
        if (assignedDeviceList != null) {
            deleteByDeviceTypeBO(deviceTypeBO);
            for (DeviceSimplifyVo deviceSimplifyVo : assignedDeviceList) {
                Device device = deviceService.selectByDevice(deviceSimplifyVo.getDevice());
                DeviceTypeItem deviceTypeItem = new DeviceTypeItem();
                deviceTypeItem.setDeviceBo(device.getBo());
                deviceTypeItem.setDeviceTypeBo(deviceTypeBO);
                deviceTypeItemMapper.insert(deviceTypeItem);
            }
        }
    }

    @Override
    public void deleteByDeviceTypeBO(String deviceTypeBO) {
        QueryWrapper<DeviceTypeItem> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(DeviceTypeItem.DEVICE_TYPE_BO,deviceTypeBO);
        deviceTypeItemMapper.delete(entityWrapper);
    }


}