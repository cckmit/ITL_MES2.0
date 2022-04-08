package com.itl.mes.core.client.service;

import com.itl.mes.core.api.dto.CustomDataValRequest;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mes.core.client.config.FallBackConfig;
import com.itl.mes.core.client.service.impl.CustomDataValServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @author yaoxiang
 * @date 2020/12/29
 * @since JDK1.8
 *
 */
@FeignClient(value = "mes-md-provider",contextId = "customDataVal",fallback = CustomDataValServiceImpl.class, configuration = FallBackConfig.class)
public interface CustomDataValService {

    /**
     * 根据 工厂、BO、数据类型查询自定义数据和自定义数据值
     * @param site
     * @param bo
     * @param dataType
     * @return
     */
    @GetMapping("/commonCustomDatas/selectCustomDataVoListByDataType/{site}/{bo}/{dataType}")
    @ApiOperation(value = "根据 工厂、BO、数据类型查询自定义数据和自定义数据值")
    List<CustomDataAndValVo> selectCustomDataAndValByBoAndDataType(
            @PathVariable("site") String site,
            @PathVariable("bo") String bo,
            @PathVariable("dataType") String dataType );

    /**
     * 保存自定义数据值
     * @param customDataValRequest
     * @return
     */
    @PostMapping("/commonCustomDatas/saveCustomDataVal")
    @ApiOperation(value = "保存自定义数据值")
    String saveCustomDataVal(CustomDataValRequest customDataValRequest);
}
