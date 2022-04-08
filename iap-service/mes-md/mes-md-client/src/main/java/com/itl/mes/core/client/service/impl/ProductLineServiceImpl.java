package com.itl.mes.core.client.service.impl;

import com.itl.mes.core.client.service.ProductLineService;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.vo.ProductLineVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author yaoxiang
 * @date 2020/12/14
 * @since JDK1.8
 */
@Slf4j
@Component
public class ProductLineServiceImpl implements ProductLineService {
    @Override
    public ResponseData<ProductLineVo> getProductLineByProductLine(String productLine) {
        log.error("sorry ProductLineService getProductLineByProductLine feign fallback productLine:{}" + productLine);
        return null;
    }
}
