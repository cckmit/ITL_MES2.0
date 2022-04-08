package com.itl.mes.pp.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.core.api.entity.ShopOrder;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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

    IPage<Map<String, Object>> getList(Page page, @Param("params") Map<String, Object> params);

    List<CustomDataAndValVo> selectCustomDataAndValByBoAndDataType(Map<String,Object> param);

    List<Map<String,Object>> getBindingBySite(String site);

    void taskLock(Map<String, Object> insertMap);

    Map<String,Object> getByOrder(String shopOrder);

    void updateOtherOrder(Map<String, Object> params);

    void insertOrder(Map<String, Object> params);

    void updateOrderById(Map<String, Object> map);


    void saveAutoSchedule(Map<String, Object> stringObjectMap);

    IPage<Map<String, Object>> getAutoList(Page page, @Param("params") Map<String, Object> params);

    void updateOrderSort(Map<String, Object> params);

    void delAutoSchedule(Map<String, Object> params);

    void updateNoLock(Map<String, Object> map);

    IPage<Map<String, Object>> getOrderByT3(Page page, @Param("params") Map<String, Object> params);

    void updateErpLot(Map<String, Object> params);

    List<Map<String, Object>> getErpLot(String site);
}