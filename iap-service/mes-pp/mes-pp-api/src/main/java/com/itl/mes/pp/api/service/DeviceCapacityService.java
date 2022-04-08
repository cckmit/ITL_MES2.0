package com.itl.mes.pp.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.pp.api.dto.ProductLineDto;
import com.itl.mes.pp.api.entity.DeviceCapacityEntity;

/**
 * 产线表
 *
 * @author cuichonghe

 * @date 2020-12-18 14:05:47
 */
public interface DeviceCapacityService extends IService<DeviceCapacityEntity> {


    /**
     * 查询全部
     *
     * @return
     */
    IPage<DeviceCapacityEntity> getAll(ProductLineDto productLineDto);

    /**
     * 更新或保存
     *
     * @param productLineEntities
     */
    void saveProductLine(DeviceCapacityEntity productLineEntities) throws CommonException;
}

