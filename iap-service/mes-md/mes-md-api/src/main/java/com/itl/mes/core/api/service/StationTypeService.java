package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.entity.StationType;
import com.itl.mes.core.api.vo.StationTypeItemVo;
import com.itl.mes.core.api.vo.StationTypeVo;


import java.util.Date;
import java.util.List;

/**
 * <p>
 * 工位类型表 服务类
 * </p>
 *
 * @author space
 * @since 2019-05-31
 */
public interface StationTypeService extends IService<StationType> {

    List<StationType> selectList();

    void save(StationTypeVo stationTypeVo)throws CommonException;
    StationType selectByStationType(String stationType)throws CommonException;
    StationTypeVo selectStationTypeVo(String stationType)throws CommonException;
    List<StationType> selectStationType(String stationType, String stationTypeName, String stationTypeDesc)throws CommonException;
    int delete(String stationType, Date modifyDate)throws CommonException;
    List<StationTypeItemVo> getStationTypeItemVo(String station)throws CommonException;
}