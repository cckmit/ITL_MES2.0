package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.bo.ProductLineHandleBO;
import com.itl.mes.core.api.entity.ProductLine;
import com.itl.mes.core.api.vo.ProductLineQueryVo;
import com.itl.mes.core.api.vo.ProductLineVo;


import java.util.Date;
import java.util.List;

/**
 * <p>
 * 产线表 服务类
 * </p>
 *
 * @author space
 * @since 2019-05-24
 */
public interface ProductLineService extends IService<ProductLine> {

    List<ProductLine> selectList();

    /**
     * 保存产线数据
     *
     * @param productLineVo productLineVo
     * @throws BusinessException 异常
     */
    void saveProductLine(ProductLineVo productLineVo) throws CommonException;

    /**
     * 通过产线删除产线相关数据
     *
     * @param productLine 产线
     * @param modifyDate 修改时间
     * @throws CommonException 异常
     */
    void deleteProductLineByProductLine(String productLine, Date modifyDate)throws CommonException;

    ProductLine selectByProductLine(String productLine)throws CommonException;

    /**
     * 通过ProductLineHandleBO查询产线数据
     *
     * @param productLineHandleBO productLineHandleBO
     * @return ProductLine
     */
    ProductLine getProductLineByHandleBO(ProductLineHandleBO productLineHandleBO);

    /**
     * 通过ProductLineHandleBO查询产线数据，未查到则报错
     *
     * @param productLineHandleBO productLineHandleBO
     * @return ProductLine
     * @throws CommonException 异常
     */
    ProductLine getExistProductLineByHandleBO(ProductLineHandleBO productLineHandleBO)throws CommonException;

    /**
     * 查询产线相关数据
     *
     * @param productLine 产线
     * @return ProductLineVo
     * @throws CommonException 异常
     */
    ProductLineVo getProductLineByProductLine(String productLine)throws CommonException;

    /**
     * 根据车间查询产线
     * @param queryVo
     * @return
     */
    List<ProductLine> listByWorkShop(ProductLineQueryVo queryVo);
}
