package com.itl.mes.me.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.me.api.dto.snLifeCycle.*;
import com.itl.mes.me.api.entity.MeSfc;
import com.itl.mes.me.api.entity.MeSfcPacking;
import com.itl.mes.me.api.entity.MeSfcPackingDetail;
import com.itl.mes.me.api.entity.MeSn;

import java.util.List;

/**
 * 车间作业控制信息表
 *
 * @author renren
 * @date 2021-01-25 14:43:35
 */
public interface MeSfcService extends IService<MeSfc> {
    /**
     * 通用更新
     *
     * @param meSfc
     */
    MeSfc updateLast(MeSfc meSfc);

    /**
     * 过站采集
     *
     * @param meSfc
     * @return
     */
    MeSn acquisition(MeSfc meSfc);

    /**
     * 基本信息
     *
     * @param sn
     * @return
     */
    BasicInfo getBasicInformation(String sn);

    /**
     * BomInfo
     *
     * @param sn
     * @return
     */
    List<BomInfo> getBomInfo(String sn);

    MeSfc getMeSfcSchedule(String bo);

    List<KeyPartsBarcode> getKeyPartsBarcode(String sn);

    ShopOrderInfo getShopOrderInfo(String sn);

    /**
     * RouterInfo
     *
     * @param sn
     * @return
     */
    RouterInfo getRouterInfo(String sn);

    /**
     * PackingInfo
     * @param packingDetail
     * @return
     */
    MeSfcPacking getPackingInfo(MeSfcPackingDetail packingDetail) throws CommonException;
}

