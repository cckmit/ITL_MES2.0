package com.itl.iap.report.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.report.api.dto.DeviceResumeDto;
import com.itl.iap.report.api.vo.DeviceResumeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface DeviceResumeMapper extends BaseMapper<DeviceResumeVo> {
    IPage<DeviceResumeVo> selectDeviceInfo(Page page, @Param("deviceResumeDto") DeviceResumeDto deviceResumeDto);

    List<String> selectDeviceTypeByDevice(@Param("device") String device);

    IPage<Map<String,String>> selectDeviceInfoByCondition(Page page, @Param("deviceResumeDto") DeviceResumeDto deviceResumeDto);

    String selectByWorkShop(@Param("workShop") String workShop);

    String selectByDeviceType(@Param("deviceType") String deviceType);
    String selectByDevice(@Param("device") String device);
}
