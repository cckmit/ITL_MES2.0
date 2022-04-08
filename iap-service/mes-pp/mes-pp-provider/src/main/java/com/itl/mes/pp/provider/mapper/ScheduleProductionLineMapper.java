package com.itl.mes.pp.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.pp.api.dto.schedule.ScheduleProductionLineQueryDTO;
import com.itl.mes.pp.api.dto.schedule.ScheduleProductionLineRespDTO;
import com.itl.mes.pp.api.entity.schedule.ScheduleProductionLineEntity;
import org.apache.ibatis.annotations.Param;

/**
 * @author liuchenghao
 * @date 2020/11/12 14:09
 */
public interface ScheduleProductionLineMapper extends BaseMapper<ScheduleProductionLineEntity> {

    IPage<ScheduleProductionLineRespDTO> findList(@Param("page") Page page, @Param("scheduleProductionLineQueryDTO") ScheduleProductionLineQueryDTO scheduleProductionLineQueryDTO);

}
