package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.CarrierItem;
import com.itl.mes.core.api.vo.CarrierItemVo;

import java.util.List;

/**
 * <p>
 * 载具物料表 服务类
 * </p>
 *
 * @author space
 * @since 2019-07-22
 */
public interface CarrierItemService extends IService<CarrierItem> {

    List<CarrierItem> selectList();

    void save(String carrierTypeBO, List<CarrierItemVo> carrierItemVos)throws CommonException;

    void delete(String carrierTypeBO)throws CommonException;

    List<CarrierItem> getCarrierItemByCarrierTypeBO(String carrierTypeBO)throws CommonException;
}