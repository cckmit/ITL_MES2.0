package com.itl.iap.mes.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.iap.mes.api.dto.prove.ProveQueryDTO;
import com.itl.iap.mes.api.dto.prove.StationProveQueryDTO;
import com.itl.iap.mes.api.dto.prove.UserProveQueryDTO;
import com.itl.iap.mes.api.entity.prove.ProveEntity;

import java.util.List;

/**
 * @auth liuchenghao
 * @date 2020/12/17
 */
public interface ProveService {


    public IPage<ProveEntity> findList(ProveQueryDTO proveQueryDTO);
    public IPage<ProveEntity> findListByState(ProveQueryDTO proveQueryDTO);


    public void save(ProveEntity proveEntity);


    public void delete(List<String> ids);


    public ProveEntity findById(String id);

    public IPage<ProveEntity>  findUserProveList(UserProveQueryDTO userProveQueryDTO);


    public IPage<ProveEntity>  findNotUserProveList(UserProveQueryDTO userProveQueryDTO);


    public IPage<ProveEntity>  findStationProveList(StationProveQueryDTO stationProveQueryDTO);


    public IPage<ProveEntity>  findNotStationProveList(StationProveQueryDTO stationProveQueryDTO);

}
