package com.itl.mes.me.provider.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.util.UUID;
import com.itl.mes.me.api.entity.MeSfc;
import com.itl.mes.me.api.entity.MeSfcNcLog;
import com.itl.mes.me.api.entity.MeSfcWipLog;
import com.itl.mes.me.api.service.MeSfcNcLogService;
import com.itl.mes.me.api.service.MeSfcService;
import com.itl.mes.me.api.service.MeSfcWipLogService;
import com.itl.mes.me.provider.mapper.MeSfcNcLogMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;


@Service("meSfcNcLogService")
public class MeSfcNcLogServiceImpl extends ServiceImpl<MeSfcNcLogMapper, MeSfcNcLog> implements MeSfcNcLogService {


    @Resource
    private MeSfcWipLogService meSfcWipLogService;
    @Resource
    private MeSfcService meSfcService;

    BigDecimal one = new BigDecimal(1);

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void saveNc(MeSfc meSfc, String ncBo, BigDecimal doneQty, BigDecimal scrapQty) {

        MeSfcNcLog meSfcNcLog = new MeSfcNcLog();

        MeSfc meSfc1 = meSfcService.updateLast(meSfc.setState("已检查")
                .setDoneQty(doneQty).setScrapQty(scrapQty.add(one)).setInputQty(doneQty.add(scrapQty).add(one)));
        MeSfcWipLog ng = meSfcWipLogService.recordLog(meSfc1, null, "NG", null);

        BeanUtil.copyProperties(meSfc1, meSfcNcLog);
        meSfcNcLog.setComponentBo(meSfc1.getItemBo());
        meSfcNcLog.setRecordTime(new Date());
        meSfcNcLog.setWipLogBo(ng.getBo());
        String[] split = ncBo.split(";");
        for (String s : split) {
            if (StrUtil.isNotEmpty(s)) {
                meSfcNcLog.setBo(UUID.uuid32());
                meSfcNcLog.setNcCodeBo(s);
                save(meSfcNcLog);
            }
        }
    }

    @Override
    public void saveOk(MeSfc meSfc, BigDecimal doneQty, BigDecimal scrapQty) {
        MeSfc meSfc1 = meSfcService.updateLast(meSfc.setState("已检查")
                .setDoneQty(doneQty.add(one)).setScrapQty(scrapQty).setInputQty(doneQty.add(scrapQty).add(one)));
        meSfcWipLogService.recordLog(meSfc1, null, "OK", null);
    }
}
