package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.core.api.entity.Sfc;
import com.itl.mes.core.api.entity.Stock;

public interface StockService extends IService<Stock> {
    /**
     *
     * @param sfcEntity
     * @param wareHouse
     * @param flag  1代表正常从MES入库，0代表ERP二次入库
     * @return
     */
    String applyStock(Sfc sfcEntity, String wareHouse,int flag);
}
