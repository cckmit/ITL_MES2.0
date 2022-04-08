package com.itl.mes.core.client.service.impl;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.entity.NcCode;
import com.itl.mes.core.client.service.NgCodeServiceFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class NgCodeServiceFeignImpl implements NgCodeServiceFeign {

    @Override
    public ResponseData<List<NcCode>> selectByListBo(List<String> boList) {
        log.error("sorry NgCodeServiceFeign selectByListBo feign fallback boList:{}" + boList);
        return null;
    }
}
