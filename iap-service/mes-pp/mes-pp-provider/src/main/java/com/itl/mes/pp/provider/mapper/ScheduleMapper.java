package com.itl.mes.pp.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.pp.api.dto.schedule.*;
import com.itl.mes.pp.api.entity.schedule.ScheduleEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author liuchenghao
 * @date 2020/11/12 9:52
 */
@Repository
public interface ScheduleMapper extends BaseMapper<ScheduleEntity> {


    IPage<ScheduleRespDTO> findList(Page page, @Param("schedule") ScheduleQueryDTO scheduleQueryDTO);


    ScheduleDetailRespDTO findById(String bo);

    ScheduleDetailRespDTO findByIdWithCount(@Param("bo") String bo, @Param("productLineBo") String productLineBo);
    /**
     * 根据车间BO查询产线
     * @param workShopBo
     * @return
     */
    IPage<ProductionLineResDTO> findProductionLine(Page page, String workShopBo);

    /**
     * 根据产线bo查询工位
     * @param productionLineBo
     * @return
     */
    List<StationResDTO> findProductionLineStation(String productionLineBo);

    /**
     * 根据产线查询排程粗略信息
     * @param query
     * @return
     */
    IPage<Map<String, Object>> getByProductLine(Page page, @Param("query") ScheduleShowDto query);
}
