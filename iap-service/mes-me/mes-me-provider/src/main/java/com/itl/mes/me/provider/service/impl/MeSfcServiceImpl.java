package com.itl.mes.me.provider.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.mes.core.api.bo.*;
import com.itl.mes.me.api.dto.snLifeCycle.*;
import com.itl.mes.me.api.entity.*;
import com.itl.mes.me.api.service.MeSfcPackingDetailService;
import com.itl.mes.me.api.service.MeSfcPackingService;
import com.itl.mes.me.api.service.MeSfcService;
import com.itl.mes.me.api.service.MeSfcWipLogService;
import com.itl.mes.me.provider.mapper.MeSfcAssyMapper;
import com.itl.mes.me.provider.mapper.MeSfcMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service("meSfcService")
public class MeSfcServiceImpl extends ServiceImpl<MeSfcMapper, MeSfc> implements MeSfcService {

    @Resource
    private MeSfcAssyMapper meSfcAssyMapper;
    @Resource
    private MeSfcMapper meSfcMapper;
    @Resource
    private UserUtil userUtil;
    @Resource
    private MeSfcWipLogService meSfcWipLogService;
    @Autowired
    private MeSfcPackingDetailService packingDetailService;
    @Autowired
    private MeSfcPackingService packingService;

    @Override
    public MeSfc updateLast(MeSfc meSfc) {

        meSfc.setProductLineBo(new ProductLineHandleBO(UserUtils.getSite(), UserUtils.getProductLine()).getBo())
                .setSite(UserUtils.getSite())
                .setStationBo(new StationHandleBO(UserUtils.getSite(), UserUtils.getStation()).getBo())
                .setWorkShopBo(new WorkShopHandleBO(UserUtils.getSite(), UserUtils.getWorkShop()).getBo())
                .setUserBo(userUtil.getUser().getUserName())
                .setOperationBo(meSfcAssyMapper.getOperationBoByStationBo(meSfc.getStationBo()))
                .setTeamBo(meSfcAssyMapper.getTeamBoByUserBo(userUtil.getUser().getUserName()))
                .setInTime(new Date());

        updateById(meSfc);
        return meSfc;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public MeSn acquisition(MeSfc meSfc) {

        MeSfc meSfc1 = updateLast(meSfc.setState("已采集"));
        MeSfcWipLog ok = meSfcWipLogService.recordLog(meSfc1, null, "OK", null);
        MeSn meSn = new MeSn();
        meSn.setSn(meSfc.getSfc());
        meSn.setCreateDate(ok.getCreateDate());
        return meSn;
    }

    @Override
    public BasicInfo getBasicInformation(String sn) {

        BasicInfo b = meSfcMapper.getBasicInformation(sn);
        if (StrUtil.isNotEmpty(b.getOperationBo())) {
            b.setOperationBo(new OperationHandleBO(b.getOperationBo()).getOperation());
        }
        if (StrUtil.isNotEmpty(b.getStationBo())) {
            b.setStationBo(new StationHandleBO(b.getStationBo()).getStation());
        }
        if (StrUtil.isNotEmpty(b.getProductLineBo())) {
            b.setProductLineBo(new ProductLineHandleBO(b.getProductLineBo()).getProductLine());
        }
        if (StrUtil.isNotEmpty(b.getWorkShopBo())) {
            b.setWorkShopBo(new WorkShopHandleBO(b.getWorkShopBo()).getWorkShop());
        }
        List<MeSfcWipLog> list = meSfcWipLogService.list(new QueryWrapper<MeSfcWipLog>().lambda().select(MeSfcWipLog::getState).orderByDesc(MeSfcWipLog::getCreateDate));
        if (CollUtil.isNotEmpty(list)) {
            b.setState2(list.get(0).getState());
        }
        return b;
    }

    @Override
    public List<BomInfo> getBomInfo(String sn) {
        return meSfcMapper.getBomInfo(sn);
    }

    @Override
    public MeSfc getMeSfcSchedule(String bo) {
        MeSfc meSfcSchedule = meSfcMapper.getMeSfcSchedule(bo);
        if (ObjectUtil.isEmpty(meSfcSchedule)) {
            return null;
        }
        if (StrUtil.isNotEmpty(meSfcSchedule.getItemBo())) {
            meSfcSchedule.setItemVersion(new ItemHandleBO(meSfcSchedule.getItemBo()).getVersion());
            meSfcSchedule.setItem(new ItemHandleBO(meSfcSchedule.getItemBo()).getItem());
        }
        if (StrUtil.isNotEmpty(meSfcSchedule.getDeviceBo())) {
            meSfcSchedule.setDevice(new DeviceHandleBO(meSfcSchedule.getDeviceBo()).getDevice());
        }

        meSfcSchedule.setDoneQty(Optional.ofNullable(meSfcSchedule.getDoneQty()).orElse(new BigDecimal("0")));
        meSfcSchedule.setSfcQty(Optional.ofNullable(meSfcSchedule.getSfcQty()).orElse(new BigDecimal("0")));
        meSfcSchedule.setInputQty(Optional.ofNullable(meSfcSchedule.getInputQty()).orElse(new BigDecimal("0")));
        meSfcSchedule.setScrapQty(Optional.ofNullable(meSfcSchedule.getScrapQty()).orElse(new BigDecimal("0")));


        meSfcSchedule.setCompletion(meSfcSchedule.getDoneQty().divide(meSfcSchedule.getInputQty(), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
        return meSfcSchedule;
    }

    @Override
    public List<KeyPartsBarcode> getKeyPartsBarcode(String sn) {
        List<KeyPartsBarcode> keyPartsBarcode = meSfcMapper.getKeyPartsBarcode(sn);
        keyPartsBarcode.forEach(x -> {
            if (StrUtil.isNotEmpty(x.getComponenetBo())) {
                x.setComponenetBo(new ItemHandleBO(x.getComponenetBo()).getItem());
            }
            if (StrUtil.isNotEmpty(x.getStationBo())) {
                x.setStationBo(new StationHandleBO(x.getStationBo()).getStation());
            }
        });
        return keyPartsBarcode;
    }

    @Override
    public ShopOrderInfo getShopOrderInfo(String sn) {
        return meSfcMapper.getShopOrderInfo(sn);
    }

    @Override
    public RouterInfo getRouterInfo(String sn) {
        return meSfcMapper.getRouterInfo(sn);
    }

    @Override
    public MeSfcPacking getPackingInfo(MeSfcPackingDetail packingDetail) throws CommonException {
        MeSfcPacking ret = packingService.getById(packingDetail.getSfcPackingBo());
        return ret;
    }
}
