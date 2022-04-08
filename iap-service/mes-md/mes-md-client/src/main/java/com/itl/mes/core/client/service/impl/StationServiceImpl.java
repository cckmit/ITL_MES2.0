package com.itl.mes.core.client.service.impl;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.vo.StationVo;
import com.itl.mes.core.client.service.StationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author yaoxiang
 * @date 2020/12/14
 * @since JDK1.8
 */
@Slf4j
@Component
public class StationServiceImpl implements StationService {
    @Override
    public ResponseData<StationVo> selectByStation(String station) {
        log.error("sorry StationService selectByStation feign fallback station:{}" + station);
        return null;
    }

    @Override
    public ResponseData<Map<String, Object>> getByStation(String station) {
        log.error("sorry StationService getByStation feign fallback station:{}" + station);
        return null;
    }
}
