package com.itl.mes.pp.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.pp.api.dto.CapacityLoadQueryDTO;
import com.itl.mes.pp.api.dto.CapacityLoadRespDTO;
import com.itl.mes.pp.api.entity.CapacityLoadEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @auth liuchenghao
 * @date 2021/1/7
 */
public interface CapacityLoadMapper extends BaseMapper<CapacityLoadEntity> {


    List<CapacityLoadRespDTO> machineRatedTime(String site, @Param("capacityLoadQueryDTO") CapacityLoadQueryDTO capacityLoadQueryDTO);


    List<CapacityLoadRespDTO> capacityLoadList(String site,String startDate,String endDate);
}
