package com.itl.mes.me.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.me.api.dto.snLifeCycle.*;
import com.itl.mes.me.api.entity.MeSfc;
import com.itl.mes.me.api.vo.DetectionSnInfoVo;
import com.itl.mes.me.api.vo.RepairSnInfoVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 车间作业控制信息表
 *
 * @author renren
 * @date 2021-01-25 14:43:35
 */
@Repository
public interface MeSfcMapper extends BaseMapper<MeSfc> {

    /**
     * BasicInfo
     *
     * @param sn
     * @return
     */
    BasicInfo getBasicInformation(@Param("sn") String sn);

    /**
     * BomInfo
     *
     * @param sn
     * @return
     */
    List<BomInfo> getBomInfo(@Param("sn") String sn);

    /**
     * ScheduleInfo
     *
     * @param bo
     * @return
     */
    MeSfc getMeSfcSchedule(@Param("bo") String bo);

    /**
     * ShopOrderInfo
     *
     * @param sn
     * @return
     */
    ShopOrderInfo getShopOrderInfo(@Param("sn") String sn);

    /**
     * RouterInfo
     *
     * @param sn
     * @return
     */
    RouterInfo getRouterInfo(@Param("sn") String sn);

    List<KeyPartsBarcode> getKeyPartsBarcode(@Param("sn") String sn);

    /**
     * SN生命周期-检测记录
     */
    List<DetectionSnInfoVo> getDetectionLifeCycleInfo(@Param("sn") String sn);

    /**
     * SN生命周期-维修记录
     */
    List<RepairSnInfoVo> getRepairLifeCycle(@Param("sn") String sn);

}
