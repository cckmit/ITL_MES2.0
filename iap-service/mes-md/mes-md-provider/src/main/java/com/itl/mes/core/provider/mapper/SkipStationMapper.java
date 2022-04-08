package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.core.api.entity.SkipStation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SkipStationMapper extends BaseMapper<SkipStation> {
    List<String> listOperationBoGroupBySfc(@Param("sfc") String sfc);
}
