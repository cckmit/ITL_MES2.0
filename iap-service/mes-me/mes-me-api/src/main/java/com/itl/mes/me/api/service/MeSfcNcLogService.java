package com.itl.mes.me.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.me.api.entity.MeSfc;
import com.itl.mes.me.api.entity.MeSfcNcLog;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Sfc不合格记录表
 *
 * @author renren
 * @date 2021-01-25 14:43:25
 */
public interface MeSfcNcLogService extends IService<MeSfcNcLog> {


    /**
     * 不合格记录
     *
     * @param meSfc
     * @param ncBo
     */
    void saveNc(MeSfc meSfc, String ncBo, BigDecimal doneQty, BigDecimal scrapQty);

    /**
     * 合格记录
     *
     * @param meSfc
     */
    void saveOk(MeSfc meSfc, BigDecimal doneQty, BigDecimal scrapQty);

}

