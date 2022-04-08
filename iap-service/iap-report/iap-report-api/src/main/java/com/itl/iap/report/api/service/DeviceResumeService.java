package com.itl.iap.report.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.report.api.dto.DeviceResumeDto;
import com.itl.iap.report.api.vo.DeviceResumeVo;

import java.util.Map;

public interface DeviceResumeService {
    IPage<DeviceResumeVo> selectDeviceInfo(DeviceResumeDto  deviceResumeDto) throws CommonException;
    IPage<Map<String,String>>  selectByCondition(DeviceResumeDto deviceResumeDto) throws CommonException;
}
