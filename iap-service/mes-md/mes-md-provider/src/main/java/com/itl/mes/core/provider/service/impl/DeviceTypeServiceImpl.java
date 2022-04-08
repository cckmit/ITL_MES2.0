package com.itl.mes.core.provider.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.CommonUtil;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.base.utils.ValidationUtil;
import com.itl.mes.core.api.bo.DeviceTypeHandleBO;
import com.itl.mes.core.api.constant.CustomCommonConstants;
import com.itl.mes.core.api.entity.Device;
import com.itl.mes.core.api.entity.DeviceType;
import com.itl.mes.core.api.service.DeviceTypeItemService;
import com.itl.mes.core.api.service.DeviceTypeService;
import com.itl.mes.core.api.vo.DeviceSimplifyVo;
import com.itl.mes.core.api.vo.DeviceTypeVo;
import com.itl.mes.core.api.vo.DeviceVo;
import com.itl.mes.core.provider.mapper.DeviceMapper;
import com.itl.mes.core.provider.mapper.DeviceTypeMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备类型表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-06-17
 */
@Service
@Transactional
public class DeviceTypeServiceImpl extends ServiceImpl<DeviceTypeMapper, DeviceType> implements DeviceTypeService {


    @Autowired
    private DeviceTypeMapper deviceTypeMapper;

    @Autowired
    private DeviceTypeItemService deviceTypeItemService;

    @Autowired
    private DeviceMapper deviceMapper;

    @Resource
    private UserUtil userUtil;

    /**
     * 验证设备类型数据是否合规
     *
     * @param deviceType
     * @throws CommonException
     */
    void validateDeviceType(DeviceType deviceType) throws CommonException {

        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(deviceType);
        if (validResult.hasErrors()) {
            throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

    }

    /**
     * 验证设备指定属性数据格式是否合规
     *
     * @param deviceType
     * @param fields
     * @throws CommonException
     */
    void validateDeviceTypeFields(DeviceType deviceType, String... fields) throws CommonException {
        ValidationUtil.ValidResult validResult = null;
        for (int i = 0; i < fields.length; i++) {
            validResult = ValidationUtil.validateProperty(deviceType, fields[i]);
            if (validResult.hasErrors()) {
                throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
        }
    }

    @Override
    public List<DeviceType> selectList() {
        QueryWrapper<DeviceType> entityWrapper = new QueryWrapper<>();
        //getEntityWrapper(entityWrapper, deviceType);
        return super.list(entityWrapper);
    }

    @Override
    public DeviceType selectByDeviceType(String deviceType) throws CommonException {
        QueryWrapper<DeviceType> entityWrapper = new QueryWrapper<>();
        entityWrapper.eq(DeviceType.SITE, UserUtils.getSite());
        entityWrapper.eq(DeviceType.DEVICE_TYPE, deviceType);
        List<DeviceType> deviceTypes = deviceTypeMapper.selectList(entityWrapper);
        if (deviceTypes.isEmpty()) {
            throw new CommonException("设备类型:" + deviceType + "未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        } else {
            return deviceTypes.get(0);
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void saveDeviceType(DeviceTypeVo deviceTypeVo) throws CommonException {
        String site = UserUtils.getSite(); //获取工厂
        DeviceTypeHandleBO deviceTypeHandleBO = new DeviceTypeHandleBO(site, deviceTypeVo.getDeviceType());
        DeviceType deviceTypeEntity = super.getById(deviceTypeHandleBO.getBo());

        if (deviceTypeEntity == null) {
            insertDeviceType(deviceTypeVo);
        } else {
            updateDeviceType(deviceTypeEntity, deviceTypeVo);
        }

        deviceTypeItemService.saveDeviceTypeItem(deviceTypeHandleBO.getBo(), deviceTypeVo.getAssignedDeviceList());

    }


    private void updateDeviceType(DeviceType deviceTypeEntity, DeviceTypeVo deviceTypeVo) throws CommonException {
        //Date frontModifyDate = deviceTypeVo.getModifyDate();
        //CommonUtil.compareDateSame(frontModifyDate, deviceTypeEntity.getModifyDate());
        Date newModifyDate = new Date();
        String site = UserUtils.getSite();
        DeviceTypeHandleBO deviceTypeHandleBO = new DeviceTypeHandleBO(site, deviceTypeVo.getDeviceType());
        DeviceType deviceType = new DeviceType();
        deviceType.setBo(deviceTypeHandleBO.getBo());
        deviceType.setSite(site);
        deviceType.setDeviceType(deviceTypeVo.getDeviceType());
        deviceType.setDeviceTypeName(deviceTypeVo.getDeviceTypeName());
        deviceType.setDeviceTypeDesc(deviceTypeVo.getDeviceTypeDesc());
        deviceType.setModifyDate(newModifyDate);
        deviceType.setModifyUser(userUtil.getUser().getUserName());
        validateDeviceTypeFields(deviceType, "deviceType", "deviceTypeName", "deviceTypeDesc");
        boolean successFlag = super.updateById(deviceType);
        if (!successFlag) {
            throw new CommonException("设备类型" + deviceTypeVo.getDeviceType() + "未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
    }

    private void insertDeviceType(DeviceTypeVo deviceTypeVo) throws CommonException {
        String site = UserUtils.getSite();
        DeviceTypeHandleBO deviceTypeHandleBO = new DeviceTypeHandleBO(site, deviceTypeVo.getDeviceType());
        DeviceType deviceType = new DeviceType();
        deviceType.setBo(deviceTypeHandleBO.getBo());
        deviceType.setSite(site);
        deviceType.setDeviceType(deviceTypeVo.getDeviceType());
        deviceType.setDeviceTypeName(deviceTypeVo.getDeviceTypeName());
        deviceType.setDeviceTypeDesc(deviceTypeVo.getDeviceTypeDesc());
        deviceType.setObjectSetBasicAttribute(userUtil.getUser().getUserName(), new Date());
        validateDeviceType(deviceType);
        deviceTypeMapper.insert(deviceType);
    }

    @Override
    public DeviceTypeVo getDeviceTypeVoByDeviceType(String deviceType) throws CommonException {
        DeviceType deviceTypeEntity = selectByDeviceType(deviceType);
        DeviceTypeVo deviceTypeVo = new DeviceTypeVo();
        BeanUtils.copyProperties(deviceTypeEntity, deviceTypeVo);
        List<Map<String, Object>> availableDeviceList = deviceTypeMapper.getAvailableDeviceList(UserUtils.getSite(), deviceTypeEntity.getBo());
        List<DeviceSimplifyVo> available = new ArrayList<>();
        if (!availableDeviceList.isEmpty()) {
            for (Map<String, Object> map : availableDeviceList) {
                DeviceSimplifyVo deviceSimplifyVo = new DeviceSimplifyVo();
                if(map.get("DEVICE") !=null){
                    deviceSimplifyVo.setDevice(map.get("DEVICE").toString());
                }
                if(map.get("DEVICE_NAME") !=null){
                    deviceSimplifyVo.setDeviceName(map.get("DEVICE_NAME").toString());
                }
                if(map.get("DEVICE_DESC") !=null){
                    deviceSimplifyVo.setDeviceDesc(map.get("DEVICE_DESC").toString());
                }

                available.add(deviceSimplifyVo);
            }
        }
        List<Map<String, Object>> assignedDeviceList = deviceTypeMapper.getAssignedDeviceList(UserUtils.getSite(), deviceTypeEntity.getBo());
        List<DeviceSimplifyVo> assigned = new ArrayList<>();
        if (!assignedDeviceList.isEmpty()) {
            for (Map<String, Object> map : assignedDeviceList) {
                DeviceSimplifyVo deviceSimplifyVo = new DeviceSimplifyVo();
                if(map.get("DEVICE") !=null){
                    deviceSimplifyVo.setDevice(map.get("DEVICE").toString());
                }
                if(map.get("DEVICE_NAME") !=null){
                    deviceSimplifyVo.setDeviceName(map.get("DEVICE_NAME").toString());
                }
                if(map.get("DEVICE_DESC") !=null){
                    deviceSimplifyVo.setDeviceDesc(map.get("DEVICE_DESC").toString());
                }
                assigned.add(deviceSimplifyVo);
            }
        }
        deviceTypeVo.setAvailableDeviceList(available);
        deviceTypeVo.setAssignedDeviceList(assigned);
        deviceTypeVo.setDeviceType(deviceTypeEntity.getDeviceType());
        deviceTypeVo.setDeviceTypeName(deviceTypeEntity.getDeviceTypeName());
        deviceTypeVo.setDeviceTypeDesc(deviceTypeEntity.getDeviceTypeDesc());
        deviceTypeVo.setModifyDate(deviceTypeEntity.getModifyDate());
        return deviceTypeVo;
    }

    @Override
    public void deleteDeviceType(String deviceType) throws CommonException {
        DeviceTypeHandleBO deviceTypeHandleBO = new DeviceTypeHandleBO(UserUtils.getSite(), deviceType);
        DeviceType deviceTypeEntity = deviceTypeMapper.selectById(deviceTypeHandleBO.getBo());
        if (deviceTypeEntity == null) {
            throw new CommonException("设备类型"+deviceType+"未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        //CommonUtil.compareDateSame(modifyDate,deviceTypeEntity.getModifyDate());
        deviceTypeMapper.deleteById(deviceTypeEntity.getBo());
        deviceTypeItemService.deleteByDeviceTypeBO(deviceTypeEntity.getBo());
    }

    @Override
    public List<DeviceVo> getDeviceVoList() {
        List<Device> devices = deviceMapper.selectTop("dongyin");
        List<DeviceVo> deviceVos = new ArrayList<>();
        if (!devices.isEmpty()) {
            for (Device device : devices) {
                DeviceVo deviceVo = new DeviceVo();
                BeanUtils.copyProperties(device, deviceVo);
                deviceVos.add(deviceVo);
            }
        }
        return deviceVos;
    }

}