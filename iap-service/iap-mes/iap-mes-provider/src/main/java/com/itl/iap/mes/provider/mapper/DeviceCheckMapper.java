package com.itl.iap.mes.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.mes.api.entity.DeviceCheck;

import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface DeviceCheckMapper extends BaseMapper<DeviceCheck> {
    IPage<DeviceCheck> selectDeviceCheckPage(Page page, @Param("deviceName") String deviceName, @Param("checkRealName") String checkRealName, @Param("ksTime") String ksTime,@Param("jsTime") String jsTime,@Param("deviceCode") String deviceCode);
    String selectMaxCode();
    DeviceCheck selectByDeviceCheckCode(@Param("deviceCheckCode") String deviceCheckCode);
}
