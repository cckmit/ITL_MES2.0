package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.bo.CarrierTypeHandleBO;
import com.itl.mes.core.api.entity.CarrierType;
import com.itl.mes.core.api.vo.CarrierTypeVo;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 载具类型表 服务类
 * </p>
 *
 * @author space
 * @since 2019-07-22
 */
public interface CarrierTypeService extends IService<CarrierType> {

    List<CarrierType> selectList();

    void save(CarrierTypeVo carrierTypeVo)throws CommonException;

    CarrierTypeVo getCarrierTypeVoByCarrierType(String carrierType)throws CommonException;

    void delete(String carrierType, Date modifyDate)throws CommonException;

    //存在返回对象，不存在返回null;
    CarrierType getCarrierType(CarrierTypeHandleBO carrierTypeHandleBO)throws CommonException;

    //存在返回对象，不存在抛BusinessException异常;
    CarrierType getExitsCarrierType(CarrierTypeHandleBO carrierTypeHandleBO)throws CommonException;
}