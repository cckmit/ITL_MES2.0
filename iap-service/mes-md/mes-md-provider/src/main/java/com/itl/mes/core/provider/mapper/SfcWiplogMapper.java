package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.core.api.dto.OpThroughRateDTO;
import com.itl.mes.core.api.entity.SfcWiplog;
import com.itl.mes.core.api.vo.OpThroughRateVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SfcWiplogMapper extends BaseMapper<SfcWiplog> {

    IPage<OpThroughRateVo> getOperationThroughRateReport(Page page,@Param("opThroughRateDTO") OpThroughRateDTO opThroughRateDTO);

    //根据工序查询不良详情
    List<OpThroughRateVo.NcDetails> selectWipNcByOperationBo(@Param("startTime")String startTime,@Param("endTime") String endTime,@Param("operationBo") String operationBo);

    //根据工序查询报废详情
    List<OpThroughRateVo.ScrapDetails> selectWipScByOperationBo(@Param("startTime")String startTime,@Param("endTime") String endTime,@Param("operationBo") String operationBo);

    List<OpThroughRateVo> getOpThroughRateByCon(@Param("opThroughRateDTO") OpThroughRateDTO opThroughRateDTO);
}
