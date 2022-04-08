package com.itl.mes.me.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.me.api.dto.MeSfcPackDto;
import com.itl.mes.me.api.entity.MeSfcPacking;

/**
 * Sfc包装
 *
 * @author renren
 * @date 2021-01-25 14:43:26
 */
public interface MeSfcPackingService extends IService<MeSfcPacking> {

    /**
     * 执行包装
     * @param meSfcPackDto
     */
    void pack(MeSfcPackDto meSfcPackDto) throws CommonException;

    /**
     * 生成箱号
     * @param item
     * @param itemVersion
     * @return
     */
    String generateNo(String item, String itemVersion);
}

