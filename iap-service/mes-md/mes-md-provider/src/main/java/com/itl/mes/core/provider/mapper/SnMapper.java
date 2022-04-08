package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.mes.core.api.entity.Sn;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 条码信息表 Mapper 接口
 * </p>
 *
 * @author space
 * @since 2019-07-25
 */
@Repository
public interface SnMapper extends BaseMapper<Sn> {

    String getSelfDefiningData(@Param("site") String site, @Param("customDataType") String customDataType, @Param("field") String field, @Param("customDataValBo") String customDataValBo);

    List<Map<String,Object>> selectPageSN(IPage<Map<String,Object>> page, @Param("params")Map<String, Object> params);

    //todo 获取工单条件是否要变?
    List<Map<String,Object>> selectPageShopOrderByShape(IPage<Map<String,Object>> page, @Param("params")Map<String, Object> params);

    Sn selectMaxSerial(@Param("site") String site, @Param("complementCodeState")String complementCodeState, @Param("ruleCode")String ruleCode, @Param("newDateSt")String newDateSt);

    void updateShopOrderSchedulQty(@Param("shopOrderBO")String shopOrderBO, @Param("qty")int qty);

    void updateShopOrderOverfulfillQty(@Param("shopOrderBO")String shopOrderBO, @Param("qty")int qty);

    /**
     *修改生产前工单返坯数
     * @param shopOrderBO
     * @param qty
     */
    void updateAttribute2ByShopOrderBoAndQty(@Param("shopOrderBO")String shopOrderBO,@Param("qty") int qty);

    /**
     * 修改生产前工单破损数
     * @param shopOrderBO
     * @param qty
     */
    void updateAttribute3ByShopOrderBoAndQty(@Param("shopOrderBO")String shopOrderBO,@Param("qty") int qty);

    /**
     * 修改生产后工单返坯数
     * @param shopOrderBO
     * @param qty
     */
    void updateAttribute4ByShopOrderBoAndQty(@Param("shopOrderBO")String shopOrderBO,@Param("qty") int qty);

    /**
     *修改生产后工单破损数
     * @param shopOrderBO
     * @param qty
     */
    void updateAttribute5ByShopOrderBoAndQty(@Param("shopOrderBO")String shopOrderBO,@Param("qty") int qty);
}