package com.itl.iap.report.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.itl.iap.report.api.dto.CommonDto;
import com.itl.iap.report.api.dto.DeviceDto;
import com.itl.iap.report.api.dto.DeviceEfficientDto;
import com.itl.iap.report.api.dto.DeviceStateDto;
import com.itl.iap.report.api.vo.DeviceStateVo;
import com.itl.iap.report.api.vo.EquipmentRunningRateVO;

import java.text.ParseException;
import java.util.List;

public interface DeviceReportService {
    DeviceEfficientDto selectDeviceEfficient();
    List<CommonDto> selectRunningSate(DeviceDto deviceDto) throws ParseException;

    IPage<DeviceStateVo> deviceStateDetail(DeviceDto deviceDto);

    IPage<DeviceStateVo> deviceState(DeviceDto deviceDto);
    /**
     * 设备稼动率数据
     * @return
     */
    PageInfo selectDeviceObjectTime(EquipmentRunningRateVO equipmentRunningRateVO);

    IPage<EquipmentRunningRateVO> selectDeviceObject(EquipmentRunningRateVO equipmentRunningRateVO);


    IPage<DeviceStateVo> deviceDetail(DeviceDto deviceDto);

    DeviceStateDto selectStateInfo(DeviceDto deviceDto);
}
