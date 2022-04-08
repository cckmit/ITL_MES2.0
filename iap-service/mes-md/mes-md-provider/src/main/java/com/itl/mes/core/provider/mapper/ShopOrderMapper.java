package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.core.api.dto.ShopOrderDTO;
import com.itl.mes.core.api.dto.ShopOrderReportDTO;
import com.itl.mes.core.api.entity.ShopOrder;
import com.itl.mes.core.api.vo.ShopOrderFullVo;
import com.itl.mes.core.api.vo.ShopOrderReportVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工单表 Mapper 接口
 * </p>
 *
 * @author space
 * @since 2019-06-17
 */
@Repository
public interface ShopOrderMapper extends BaseMapper<ShopOrder> {

    Integer updateShopOrder(@Param("shopOrder") ShopOrder shopOrder, @Param("changeDate") Date changeDate);

    Integer updateLimitQtyShopOrderReleaseQtyByBO( @Param( "bo" ) String bo,@Param( "qty" )BigDecimal qty,@Param( "overfullQty" )BigDecimal overfullQty );

    Integer updateOverfullQtyShopOrderReleaseQtyByBO( @Param( "bo" ) String bo,@Param( "qty" )BigDecimal qty );

    Integer updateShopOrderReleaseQtyByBO( @Param( "bo" ) String bo,@Param( "qty" )BigDecimal qty );

    Integer updateShopOrderCompleteQtyByBO( @Param( "bo" ) String bo,@Param( "completeQty" )BigDecimal completeQty );

    Integer updateShopOrderScrapQtyByBO( @Param( "bo" ) String bo,@Param( "scrapTty" )BigDecimal scrapTty );

    IPage<ShopOrder> getList(Page page, @Param("params") Map<String,Object> params);

    List<String> getIdsByVals(Map<String, Object> result);

    List<Map<String, Object>> getBindingBySite(String site);

    void updateEmergenc(Map<String, Object> params);

    List<String> getScheduleShopOrder();

    IPage<ShopOrder> selectShopOrderRelease(Page page, @Param("shopOrderDTO") ShopOrderDTO shopOrderDTO);

    IPage<ShopOrder> selectShopOrderReleaseByZz(Page page, @Param("shopOrderDTO") ShopOrderDTO shopOrderDTO);

    IPage<ShopOrderReportVo> getShopOrderReport(Page page, @Param("shopOrderReportDTO") ShopOrderReportDTO shopOrderReportDTO);

    List<ShopOrderReportVo.MakingDetailsVo> getMakingDetails(@Param("shopOrderBo") String shopOrderBo);

    List<ShopOrder> getShopOrder(@Param("shopOrderBo") String shopOrderBo);
}