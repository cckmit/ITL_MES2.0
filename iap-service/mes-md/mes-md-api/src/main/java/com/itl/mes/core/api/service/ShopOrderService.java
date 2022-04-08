package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.bo.ShopOrderHandleBO;
import com.itl.mes.core.api.dto.DispatchDTO;
import com.itl.mes.core.api.dto.OperationOrderDTO;
import com.itl.mes.core.api.dto.ShopOrderDTO;
import com.itl.mes.core.api.dto.ShopOrderReportDTO;
import com.itl.mes.core.api.entity.Dispatch;
import com.itl.mes.core.api.entity.OperationOrder;
import com.itl.mes.core.api.entity.ShopOrder;
import com.itl.mes.core.api.vo.ShopOrderFullVo;
import com.itl.mes.core.api.vo.ShopOrderReportVo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工单表 服务类
 * </p>
 *
 * @author space
 * @since 2019-06-17
 */
public interface ShopOrderService extends IService<ShopOrder> {

    List<ShopOrder> selectList();

    /**
     * 通过ShopOrderHandleBO查询存在的工单
     *
     * @param shopOrderHandleBO
     * @return
     * @throws CommonException
     */
    ShopOrder getExistShopOrder(ShopOrderHandleBO shopOrderHandleBO)throws CommonException;

    /**
     * 通过ShopOrderHandleBO查询工单
     *
     * @param shopOrderHandleBO
     * @return
     */
    ShopOrder getShopOrder(ShopOrderHandleBO shopOrderHandleBO);

    /**
     * 查询工单相关数据
     *
     * @param shopOrderHandleBO
     * @return
     * @throws CommonException
     */
    ShopOrderFullVo getShopFullOrder(ShopOrderHandleBO shopOrderHandleBO) throws CommonException;

    /**
     * 保存工单相关数据
     *
     * @param shopOrderFullVo
     * @return
     * @throws CommonException
     */
    void saveShopOrder(ShopOrderFullVo shopOrderFullVo) throws CommonException;

    /**
     * 删除工单数据
     *
     * @param shopOrderHandleBO
     * @param modifyDate
     */
    void deleteShopOrderByHandleBO(ShopOrderHandleBO shopOrderHandleBO, Date modifyDate) throws CommonException;


    //工单下达
    //List<Sfc> shopOrderRelease(String shopOrder, BigDecimal stayNumber, String planStartDate, String planEndDate) throws BusinessException;

    List<ShopOrder> getShopOrderByBomBO(String bomBO);

    //根据Sfc和数量工单下达
    //List<Sfc> shopOrderReleaseBySfcAndNumVos(String shopOrder, String planStartDate, String planEndDate, List<SfcAndNumVo> sfcAndNumVos) throws BusinessException;

    //List<Sfc> shopOrderReleaseBySfcAndNumVos(String shopOrder, String planStartDate, String planEndDate,String routerBo, List<SfcAndNumVo> sfcAndNumVos) throws BusinessException;

    /**
     * 更新工单完成数量
     *
     * @param bo 工单BO
     * @param completeQty 完成数量
     * @return Integer
     */
    Integer updateShopOrderCompleteQtyByBO(String bo, BigDecimal completeQty);

    /**
     * 更新工单报废数量
     *
     * @param bo 工单BO
     * @param scrapTty 报废数量
     * @return Integer
     */
    Integer updateShopOrderScrapQtyByBO(String bo, BigDecimal scrapTty);

    Object getAllOrder(Map<String,Object> params, Integer page, Integer pageSize);

    void updateShopOrderFullVo(ShopOrderFullVo shopOrderFullVo);

    void updateEmergenc(List<Map<String, Object>> shopOrderList);

    void updateFixedTime(String shopOrder, String fixedTime);

    IPage<ShopOrder> queryShopOrderRelease(ShopOrderDTO shopOrderDTO);

    void orderReleaseBatch(List<ShopOrderDTO> shopOrderDTOs);

    IPage<ShopOrderReportVo> getShopOrderReport(ShopOrderReportDTO shopOrderReportDTO);

    IPage<OperationOrder> getCanCancelOperationOrder(ShopOrderDTO shopOrderDTO);

    void okCancelOperationOrder(List<String> operationOrderList) throws CommonException;
}