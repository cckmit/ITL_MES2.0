package com.itl.mes.core.client.service.impl;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.entity.WorkShop;
import com.itl.mes.core.api.vo.WorkShopVo;
import com.itl.mes.core.client.service.WorkShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yaoxiang
 * @date 2020/12/14
 * @since JDK1.8
 */
@Component
@Slf4j
public class WorkShopServiceImpl implements WorkShopService {
    @Override
    public ResponseData<WorkShopVo> getWorkShopByWorkShop(String workShop) {
        log.error("sorry WorkShopService getWorkShopByWorkShop feign fallback workShop:{} " + workShop);
        return null;
    }

    @Override
    public List<WorkShop> getWorkShopBySite() {
        log.error("sorry WorkShopService getWorkShopBySite feign fallback workShop:{} " );
        return null;
    }
}
