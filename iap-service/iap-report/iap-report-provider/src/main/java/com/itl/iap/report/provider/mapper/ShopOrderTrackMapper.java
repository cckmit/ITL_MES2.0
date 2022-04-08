package com.itl.iap.report.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.report.api.dto.ShopOrderTrackDto;
import com.itl.iap.report.api.entity.ShopOrderTrack;
import com.itl.mes.core.api.entity.RouterProcessTable;
import com.itl.mes.core.api.entity.SfcStateTrack;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface ShopOrderTrackMapper extends BaseMapper<ShopOrderTrack> {
   IPage<ShopOrderTrack> selectByCondition(Page page, @Param("shopOrderTackDto")ShopOrderTrackDto shopOrderTrackDto);

   List<ShopOrderTrack> selectListByCondition(@Param("shopOrderTackDto")ShopOrderTrackDto shopOrderTrackDto);

   String selectProcessInfoBySfc(@Param("sfc") String sfc);

   String selectMaxTime(@Param("shopOrderBo") String shopOrderBo,@Param("sfc") String sfc,@Param("operationBo") String operationBo);

   int selectCountSfcByShopOrder(@Param("shopOrder") String shopOrder);

   IPage<ShopOrderTrack> selectFirstOperationByCondifition(Page page, @Param("shopOrderTackDto")ShopOrderTrackDto shopOrderTrackDto);

   Date selectMaxAllTime(@Param("shopOrderBo") String shopOrderBo, @Param("sfc") String sfc, @Param("operationBo") String operationBo);

   Date getInTimeBySfcAndOperation(@Param("sfc") String sfc,@Param("operationBo") String operationBo);

   List<RouterProcessTable> selectOperationListInfo(@Param("routerBo") String routerBo);

   SfcStateTrack selectOperationStateInfo(@Param("sfc") String sfc,@Param("operationBo") String operationBo);

   String selectNameByOperationBo(@Param("operationBo") String operationBo);

   List<HashMap<String, BigDecimal>> selectNumberProgress(@Param("sfc") String sfc,@Param("operationBo") String operationBo);
}
