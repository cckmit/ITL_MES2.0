package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.core.api.dto.SfcDto;
import com.itl.mes.core.api.entity.Sfc;
import com.itl.mes.core.api.entity.SfcWiplog;
import com.itl.mes.core.api.entity.WorkShop;
import com.itl.mes.core.api.vo.ReworkVo;
import com.itl.mes.core.api.vo.SfcVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface SfcMapper  extends BaseMapper<Sfc> {

    List<Sfc> selectSfc(@Param("operationOrder") String operationOrder);

    IPage<Sfc> selectSfcPage(Page page,@Param("operationOrder") String operationOrder,@Param("scDate") String scDate,@Param("jsDate") String jsDate,@Param("dispatchCode") String dispatchCode);

    Sfc selectBySfc(@Param("sfc") String sfc);

    String checkOperation(@Param("sfc") String sfc);

    SfcVo selectSfcInfo(@Param("sfc") String sfc);

    List<ReworkVo> selectReworkPartOne(@Param("sfc") String sfc,@Param("operationBo") String operationBo,@Param("stationBo") String stationBo);

    ReworkVo selectReworkPartTwo(@Param("sfc") String sfc);

    List<Map<String, Object>> selectSfcList(@Param("sfc") String sfc);

    Map<String, BigDecimal> selectSum(@Param("param") Map param);

    List<String> querySfcList(@Param("param") Map<String, Object> params);

    List<Map<String, Object>> selectOperationOrder(@Param("param") Map<String, Object> params);

    int getOutQty(@Param("sfc") String sfc);

    List<Sfc> getSfcByOperationOrder(@Param("param") Map<String, Object> params);

    Sfc getSfcInfoBySfc(@Param("sfc") String sfc);

    String selectWorkShopBoByUserId(@Param("userId") String userId);

    List<String> listAllSendMsgUser(@Param("workShopName") String workShopName,@Param("typeName") String typeName);
}
