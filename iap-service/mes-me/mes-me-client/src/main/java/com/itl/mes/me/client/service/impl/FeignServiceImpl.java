package com.itl.mes.me.client.service.impl;

import com.itl.mes.core.api.dto.CodeGenerateDto;
import com.itl.mes.core.api.dto.ItemForParamQueryDto;
import com.itl.mes.core.api.vo.BomVo;
import com.itl.mes.me.client.service.FeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author yaoxiang
 * @date 2021/1/22
 * @since JDK1.8
 */
@Slf4j
@Service
public class FeignServiceImpl implements FeignService {
    @Override
    public Map<String, Object> getParams(ItemForParamQueryDto queryDto) {
        log.error("sorry FeginService getParams feign fallback queryDto:{}" + queryDto.toString());
        return null;
    }

    @Override
    public List<String> generatorNextNumberList(CodeGenerateDto codeGenerateDto) {
        log.error("sorry FeginService generatorNextNumberList feign fallback codeGenerateDto:{}" + codeGenerateDto.toString());
        return null;
    }

    @Override
    public BomVo selectByBo(String bo) {
        log.error("sorry FeginService selectByBo feign fallback bo:{}" + bo);
        return null;
    }
}
