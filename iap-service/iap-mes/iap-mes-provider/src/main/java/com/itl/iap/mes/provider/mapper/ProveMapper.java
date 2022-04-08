package com.itl.iap.mes.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.mes.api.dto.prove.StationProveQueryDTO;
import com.itl.iap.mes.api.dto.prove.UserProveQueryDTO;
import com.itl.iap.mes.api.entity.prove.ProveEntity;
import org.apache.ibatis.annotations.Param;

/**
 * @author liuchenghao
 * @date 2020/11/3 17:44
 */
public interface ProveMapper extends BaseMapper<ProveEntity> {



    IPage<ProveEntity> findUserProveList(Page page,@Param("userProve") UserProveQueryDTO userProveQueryDTO);

    IPage<ProveEntity> findNotUserProveList(Page page,@Param("userProve")UserProveQueryDTO userProveQueryDTO);

    IPage<ProveEntity> findStationProveList(Page page,@Param("stationProve")StationProveQueryDTO stationProveQueryDTO);

    IPage<ProveEntity> findNotStationProveList(Page page,@Param("stationProve")StationProveQueryDTO stationProveQueryDTO);
}
