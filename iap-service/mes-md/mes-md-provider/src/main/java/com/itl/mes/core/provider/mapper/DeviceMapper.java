package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.core.api.dto.DeviceDto;
import com.itl.mes.core.api.entity.Device;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备表 Mapper 接口
 * </p>
 *
 * @author space
 * @since 2019-06-17
 */

public interface DeviceMapper extends BaseMapper<Device> {

    /**
     * 查询可分配设备数据
     * @param site
     * @param deviceBo
     * @return
     */
    List<Map<String,Object>> getAvailableDeviceTypeList(@Param("site") String site, @Param("deviceBo") String deviceBo);

    List<Map<String,Object>> getAssignedDeviceTypeList(@Param("site") String site, @Param("deviceBo") String deviceBo);

    List<Device> selectTop(@Param("site")String site);


    /**
     * 查询机台车间
     * @param page
     * @param deviceDto
     * @return
     */
    IPage<Device> selectDeviceWorkshop(Page page, @Param("device") DeviceDto deviceDto);

    IPage<Map<String,String>> getScrewByLine(Page page, @Param("params") Map<String, Object> params);

    List<Device> selectByCondition(@Param("device") DeviceDto deviceDto);

    List<String> selectProductLineBoByDevice(@Param("device") String device);
}