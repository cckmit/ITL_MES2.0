package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.bo.PackingHandleBO;
import com.itl.mes.core.api.entity.PackLevel;
import com.itl.mes.core.api.entity.Packing;
import com.itl.mes.core.api.vo.PackLevelVo;
import com.itl.mes.core.api.vo.PackingVo;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 包装定义表 服务类
 * </p>
 *
 * @author space
 * @since 2019-07-12
 */
public interface PackingService extends IService<Packing> {

    List<Packing> selectList();

    /**
     * 查询Packing
     * 存在返回对象
     * 不存在返回null
     * @param packingHandleBO
     * @return
     * @throws CommonException
     */
    Packing getPackingByPackingHandleBO(PackingHandleBO packingHandleBO) throws CommonException;


    /**
     * 查询Packing
     * 存在返回对象
     * 不存在抛异常
     * @param packingHandleBO
     * @return
     * @throws CommonException
     */
    Packing getExitsPackingByPackingHandleBO(PackingHandleBO packingHandleBO) throws CommonException;


    void save(PackingVo packingVo)throws CommonException;

    PackingVo getPackingVoByPackName(String packingName)throws CommonException;

    void delete(String packingName, Date modifyDate)throws CommonException;

    PackLevelVo getPackLevelByP(PackLevelVo packLevelVo, PackLevel packLevel)throws CommonException;

    PackLevelVo getPackLevelByM(PackLevelVo packLevelVo, PackLevel packLevel)throws CommonException;


}