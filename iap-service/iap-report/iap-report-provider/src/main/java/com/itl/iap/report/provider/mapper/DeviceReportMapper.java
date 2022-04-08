package com.itl.iap.report.provider.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.report.api.dto.DeviceDto;
import com.itl.iap.report.api.entity.TScadaData;
import com.itl.iap.report.api.vo.DeviceStateVo;
import com.itl.iap.report.api.vo.EquipmentRunningRateVO;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DeviceReportMapper {
    Map<String, Integer>  getAllCount();
    List<TScadaData> selectByTimes(@Param("device")String device,@Param("startTime") String startTime,@Param("endTime") String endTime);
    String selectMaxTimeByDate(String date);

    List<DeviceStateVo> deviceStateDetail(@Param("deviceDto") DeviceDto deviceDto);

    IPage<DeviceStateVo> deviceState(Page page, @Param("deviceDto") DeviceDto deviceDto);

    List<DeviceStateVo> deviceDetail(@Param("deviceDto") DeviceDto deviceDto);

    List<DeviceStateVo>  selectStateInfo(@Param("deviceDto") DeviceDto  deviceDto);

    List<String> selectAllDeviceState();

    /**
     * 查询设备基础信息数据
     */
    List<EquipmentRunningRateVO> selectDeviceObjectTime(@Param("vo") EquipmentRunningRateVO equipmentRunningRateVO);

    /**
     * 查询设备基础信息数据-明天List<EquipmentRunningRateVO>
     */
    List<EquipmentRunningRateVO> selectDeviceObjectTomorrow(@Param("list") Set<String> device, @Param("endTime") String endTime);

    /**
     * 查询设备基础信息数据-昨天
     */
    List<EquipmentRunningRateVO> selectDeviceObjectYesterday(@Param("list") Set<String> device,@Param("startTime") String startTime);

    /**
     * 查询设备基础信息数据-明细
     */
    IPage<EquipmentRunningRateVO> selectDeviceObject(Page page,@Param("vo") EquipmentRunningRateVO equipmentRunningRateVO);


}
