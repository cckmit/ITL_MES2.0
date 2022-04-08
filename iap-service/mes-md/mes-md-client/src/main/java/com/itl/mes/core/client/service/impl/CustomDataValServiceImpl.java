package com.itl.mes.core.client.service.impl;

import com.itl.mes.core.client.service.CustomDataValService;
import com.itl.mes.core.api.dto.CustomDataValRequest;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yaoxiang
 * @date 2020/12/29
 * @since JDK1.8
 */
@Component
@Slf4j
public class CustomDataValServiceImpl implements CustomDataValService {

    @Override
    public List<CustomDataAndValVo> selectCustomDataAndValByBoAndDataType(String site, String bo, String dataType) {
        log.error("sorry CustomDataValService selectCustomDataAndValByBoAndDataType feign fallback site:{} " + site + ",bo:{}" + bo + ",dataType:{}" + dataType);
        return null;
    }

    @Override
    public String saveCustomDataVal(CustomDataValRequest customDataValRequest) {
        log.error("sorry CustomDataValService saveCustomDataVal feign fallback customDataValRequest:{}" + customDataValRequest.toString());
        return null;
    }
}
