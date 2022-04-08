package com.itl.mes.me.provider.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.utils.UserUtil;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.util.UUID;
import com.itl.mes.core.api.bo.ItemHandleBO;
import com.itl.mes.core.api.bo.ProductLineHandleBO;
import com.itl.mes.core.api.bo.StationHandleBO;
import com.itl.mes.core.api.bo.WorkShopHandleBO;
import com.itl.mes.me.api.dto.MeSfcPackDto;
import com.itl.mes.me.api.entity.*;
import com.itl.mes.me.api.service.*;
import com.itl.mes.me.provider.mapper.MeSfcAssyMapper;
import com.itl.mes.me.provider.mapper.MeSfcPackingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class MeSfcPackingServiceImpl extends ServiceImpl<MeSfcPackingMapper, MeSfcPacking> implements MeSfcPackingService {

    @Autowired
    private MeSfcPackingDetailService meSfcPackingDetailService;

    @Autowired
    private UserUtil userUtil;

    @Resource
    private MeSfcAssyMapper meSfcAssyMapper;

    @Autowired
    private MeSfcService meSfcService;

    @Autowired
    private MeSfcWipLogService meSfcWipLogService;

    @Autowired
    private ItemRuleLabelService itemRuleLabelService;

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void pack(MeSfcPackDto meSfcPackDto) throws CommonException {
        String site = UserUtils.getSite();
        String userName = userUtil.getUser().getUserName();
        Date createTime = new Date();
        if (meSfcPackDto.getCurrentCount().compareTo(meSfcPackDto.getMaxCount()) > 0) {
            throw new CommonException("当前数量异常!", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        MeSfcPacking meSfcPacking = new MeSfcPacking();

        // insert me_sfc_packing_detail.
        meSfcPacking.setBo(UUID.uuid32())
                .setSite(site)
                .setCartonNo(meSfcPackDto.getCartonNo())
                .setMaxCount(meSfcPackDto.getMaxCount())
                .setCurrentCount(meSfcPackDto.getCurrentCount())
                .setState(meSfcPackDto.getCurrentCount().compareTo(meSfcPackDto.getMaxCount()) == 0 ? 1 : 0)
                .setCreateTime(meSfcPackDto.getcreateTime())
                .setFinishTime(meSfcPackDto.getfinishTime())
                .setRemark(meSfcPackDto.getRemark());
        Supplier<MeSfcPackingDetail> supplier = MeSfcPackingDetail::new;
        List<MeSfcPackingDetail> list = meSfcPackDto.getSns().stream()
                .map(x -> supplier.get()
                        .setBo(UUID.uuid32())
                        .setSite(site)
                        .setSfcPackingBo(meSfcPacking.getBo())
                        .setSfc(x))
                .collect(Collectors.toList());
        meSfcPackingDetailService.saveBatch(list);

        // update SFC.
        MeSfc meSfc = new MeSfc();
        meSfc.setProductLineBo(new ProductLineHandleBO(site, UserUtils.getProductLine()).getBo())
                .setSite(site)
                .setState("已包装")
                .setStationBo(new StationHandleBO(site, UserUtils.getStation()).getBo())
                .setWorkShopBo(new WorkShopHandleBO(site, UserUtils.getWorkShop()).getBo())
                .setUserBo(userName)
                .setOperationBo(meSfcAssyMapper.getOperationBoByStationBo(meSfc.getStationBo()))
                .setTeamBo(meSfcAssyMapper.getTeamBoByUserBo(userName))
                .setInTime(createTime);
        meSfcService.update(meSfc, new QueryWrapper<MeSfc>().lambda().in(MeSfc::getSfc, meSfcPackDto.getSns()));

        // insert me_sfc_wip_log.
        MeSfc one = meSfcService.getOne(new QueryWrapper<MeSfc>().lambda()
                .eq(MeSfc::getSfc, meSfcPackDto.getSns().get(0)));
        MeSfcWipLog packLog = new MeSfcWipLog();
        BeanUtil.copyProperties(one, packLog);
        packLog.setBo(UUID.uuid32())
                .setSfc(meSfcPackDto.getCartonNo())
                .setOutTime(null)
                .setCreateDate(createTime)
                .setResult("ok")
                .setIsRework(null);
        meSfcWipLogService.save(packLog);

        // insert me_sfc_packing.
        meSfcPacking.setWipLogBo(packLog.getBo());
        super.save(meSfcPacking);
    }

    @Override
    public String generateNo(String item, String itemVersion) {
        String site = UserUtils.getSite();
        ItemHandleBO itemHandleBO = new ItemHandleBO(site, item, itemVersion);

        ItemRuleLabel itemRuleLabel = itemRuleLabelService.getOne(new QueryWrapper<ItemRuleLabel>()
                .lambda()
                .eq(ItemRuleLabel::getItemBo, itemHandleBO.getBo())
                .eq(ItemRuleLabel::getLabelType, "Carton")
        );
        List<String> list = itemRuleLabelService.generatorCode(itemRuleLabel.getBo(), 1);
        AtomicReference<String> ret = new AtomicReference<>();

        Optional.ofNullable(list)
                .ifPresent(x -> ret.set(x.get(0)));
        return ret.get();
    }
}
