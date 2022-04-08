package com.itl.mes.pp.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.pp.api.dto.MachineProductCapacityDto;
import com.itl.mes.pp.api.entity.DeviceItemCapacityEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author cuichonghe
 * @date 2020-12-17 11:28:39
 */
public interface DeviceItemCapacityMapper extends BaseMapper<DeviceItemCapacityEntity> {
    /**
     * 查询分页
     *
     * @param page
     * @param machineProductCapacityDto
     * @return
     */
    IPage<DeviceItemCapacityEntity> getPage(Page page, @Param("machineProductCapacityDto") MachineProductCapacityDto machineProductCapacityDto);

    /**
     * 查寻全部
     * @param machineProductCapacityDto
     * @param site
     * @return
     */
    List<DeviceItemCapacityEntity> getAll(@Param("machineProductCapacityDto") MachineProductCapacityDto machineProductCapacityDto,@Param("site")String site);

    /**
     * 查询一条
     *
     * @param bo
     * @return
     */
    List<DeviceItemCapacityEntity> getOneById(@Param("bo") String bo);
}
