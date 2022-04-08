package com.itl.mes.me.provider.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.util.UUID;
import com.itl.mes.core.api.bo.ItemHandleBO;
import com.itl.mes.core.api.bo.ShopOrderHandleBO;
import com.itl.mes.core.api.bo.StationHandleBO;
import com.itl.mes.me.api.dto.snLifeCycle.StationRecord;
import com.itl.mes.me.api.entity.MeSfc;
import com.itl.mes.me.api.entity.MeSfcWipLog;
import com.itl.mes.me.api.service.MeSfcWipLogService;
import com.itl.mes.me.provider.mapper.MeSfcWipLogMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service("meSfcWipLogService")
public class MeSfcWipLogServiceImpl extends ServiceImpl<MeSfcWipLogMapper, MeSfcWipLog> implements MeSfcWipLogService {


    @Resource
    private MeSfcWipLogMapper meSfcWipLogMapper;

    @Override
    public List<StationRecord> getStationRecord(String sn) {
        List<StationRecord> stationRecord = meSfcWipLogMapper.getStationRecord(sn);
        stationRecord.forEach(x -> {
            if (StrUtil.isNotBlank(x.getShopOrderBo())) {
                x.setShopOrderBo(new ShopOrderHandleBO(x.getShopOrderBo()).getShopOrder());
            }
            if (StrUtil.isNotBlank(x.getStationBo())) {
                x.setStationBo(new StationHandleBO(x.getStationBo()).getStation());
            }
            if (StrUtil.isNotBlank(x.getItemBo())) {
                x.setItemBo(new ItemHandleBO(x.getItemBo()).getItem());
            }
        });

        return stationRecord;
    }

    @Override
    public MeSfcWipLog recordLog(MeSfc meSfc, Date outTime, String result, String isRework) {

        MeSfcWipLog meSfcWipLog = new MeSfcWipLog();
        BeanUtil.copyProperties(meSfc, meSfcWipLog);
        meSfcWipLog.setBo(UUID.uuid32())
                .setOutTime(outTime)
                .setCreateDate(new Date())
                .setResult(result)
                .setIsRework(isRework);
        save(meSfcWipLog);
        return meSfcWipLog;
    }
}
