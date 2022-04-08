package com.itl.iap.report.provider.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.report.api.vo.OperationTestQualifiedRateVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface OperationReportMapper {
    //分页查询工序检验合格率信息
    IPage<OperationTestQualifiedRateVo> getPageListOperationTestQualifiedRate(Page page,@Param("operationTestQualifiedRateVo") OperationTestQualifiedRateVo operationTestQualifiedRateVo);
    int sumDoneQtyByOperationBoAndSfc(@Param("operationBo") String operationBo,@Param("sfc") String sfc);
    List<String> getOutStationUserByOperationBoAndSfc(@Param("operationBo") String operationBo,@Param("sfc") String sfc);
}
