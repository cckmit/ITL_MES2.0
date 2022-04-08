package com.itl.mes.me.client.service;

import com.itl.mes.core.api.dto.CodeGenerateDto;
import com.itl.mes.core.api.dto.ItemForParamQueryDto;
import com.itl.mes.core.api.vo.BomVo;
import com.itl.mes.me.client.config.FallBackConfig;
import com.itl.mes.me.client.service.impl.FeignServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author yaoxiang
 * @date 2021/1/22
 * @since JDK1.8
 */
@FeignClient(value = "mes-md-provider",
        contextId = "meFeignService",
        fallback = FeignServiceImpl.class,
        configuration = FallBackConfig.class
)
public interface FeignService {
    /**
     * 根据字段和物料id获取各个字段的值
     * @param queryDto
     * @return
     */
    @PostMapping("/items/getParams")
    @ApiOperation("根据字段和物料id获取各个字段的值")
    Map<String, Object> getParams(@RequestBody ItemForParamQueryDto queryDto);

    /**
     * 获取number个下一编码
     * @param codeGenerateDto
     * @return
     */
    @PostMapping("/codeRules/generatorNextNumbers")
    @ApiOperation("获取number个下一编码")
    List<String> generatorNextNumberList(@RequestBody CodeGenerateDto codeGenerateDto);

    @GetMapping("/bom/queryByBo")
    @ApiOperation(value = "查询物料清单ByBo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bo", value = "bo", dataType = "string", paramType = "query")
    })
    BomVo selectByBo(@RequestParam("bo") String bo);
}
