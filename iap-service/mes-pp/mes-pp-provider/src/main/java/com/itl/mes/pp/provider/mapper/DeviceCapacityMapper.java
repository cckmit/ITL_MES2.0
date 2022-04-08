package com.itl.mes.pp.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.pp.api.entity.DeviceCapacityEntity;

import java.util.List;

/**
 * 产线表
 * 
 * @author cuichonghe
 * @date 2020-12-18 14:05:47
 */
public interface DeviceCapacityMapper extends BaseMapper<DeviceCapacityEntity> {
    /**
     * 查询全部
     * @return
     */
    List<DeviceCapacityEntity> getAll(String site);
}
