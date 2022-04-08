package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.core.api.dto.MyDeviceDto;
import com.itl.mes.core.api.entity.DeviceType;
import com.itl.mes.core.api.entity.MyDevice;
import com.itl.mes.core.api.vo.MyDeviceVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MyDeviceMapper extends BaseMapper<MyDevice> {
    List<MyDeviceVo> selectMyDevice(@Param("myDeviceDto") MyDeviceDto myDeviceDto);
    List<DeviceType> selectDeviceTypeByDeviceBo(@Param("deviceBo") String deviceBo);

    void updateFirstInsState(@Param("userId") String userId,@Param("stationBo") String stationBo,@Param("deviceBo") String deviceBo);

    //修改所有的首检状态
    void updateAllFirstInsState();
}
