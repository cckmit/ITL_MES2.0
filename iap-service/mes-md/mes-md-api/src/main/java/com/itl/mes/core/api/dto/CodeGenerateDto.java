package com.itl.mes.core.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author yaoxiang
 * @date 2021/1/22
 * @since JDK1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CodeGenerateDto {
    private String codeRuleBo;
    private int number;
    private Map<String, Object> paramMap;
}
