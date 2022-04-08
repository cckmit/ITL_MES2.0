package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.core.api.dto.OutSourceDto;
import com.itl.mes.core.api.entity.OutSource;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OutSourceMapper extends BaseMapper<OutSource> {
    IPage<OutSource>  selectByCondition(Page page, @Param("outSourceDto") OutSourceDto outSourceDto);

    String selectMaxCode();

    OutSource selectByBo(@Param("bo") String bo);

    List<String> selectExeceptionCode(@Param("execeptionCode") String execeptionCode);
    Map<String,String> selectInfoByCode(@Param("exceptionCode") String exceptionCode);
    Map<String,String> selectInfoByDevice(@Param("device") String device);
}
