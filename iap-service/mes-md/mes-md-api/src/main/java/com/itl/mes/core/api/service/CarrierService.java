package com.itl.mes.core.api.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.bo.CarrierHandleBO;
import com.itl.mes.core.api.entity.Carrier;
import com.itl.mes.core.api.vo.CarrierVo;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 载具表 服务类
 * </p>
 *
 * @author space
 * @since 2019-07-23
 */
public interface CarrierService extends IService<Carrier> {

    List<Carrier> selectList();

    void save(CarrierVo carrierVo)throws CommonException;

    CarrierVo getCarrierVoByCarrier(String carrier)throws CommonException;

    void delete(String carrier, Date modifyDate)throws CommonException;

    //存在返回对象，不存在返回null
    Carrier getCarrierHandleBO(CarrierHandleBO carrierHandleBO)throws  CommonException;

    //存在返回对象，不存在抛异常
    Carrier getExitsCarrierHandleBO(CarrierHandleBO carrierHandleBO)throws CommonException;

    /**
     *增加载具使用次数
     *
     * @param carrierBo 载具BO
     * @param useCount 次数
     * @return int
     */
    int carrierAddUseCount(String carrierBo, int useCount );
}