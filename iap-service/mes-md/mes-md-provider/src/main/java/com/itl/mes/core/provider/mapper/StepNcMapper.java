package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.core.api.dto.StepNcRecordDto;
import com.itl.mes.core.api.entity.StepNc;
import com.itl.mes.core.api.vo.StepNcRecordVo;
import org.apache.ibatis.annotations.Param;

public interface StepNcMapper extends BaseMapper<StepNc> {
    IPage<StepNcRecordVo> getStepNcRecord(Page page,@Param("stepNcRecordDto") StepNcRecordDto stepNcRecordDto);
}
