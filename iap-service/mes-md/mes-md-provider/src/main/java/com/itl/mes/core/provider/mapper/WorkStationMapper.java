package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.core.api.dto.WorkStationDTO;
import com.itl.mes.core.api.entity.WorkStation;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface WorkStationMapper extends BaseMapper<WorkStation> {
    IPage<WorkStation> selectByCondition(Page page, @Param("workStation") WorkStationDTO workStationDTO);
    IPage<WorkStation> selectByWorkingProcess(Page page,@Param("operationList") List<String> operationList);
}
