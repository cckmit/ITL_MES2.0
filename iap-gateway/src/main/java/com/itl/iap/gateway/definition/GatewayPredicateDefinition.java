package com.itl.iap.gateway.definition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 路由断言实体
 *
 * @author 汤俊
 * @date 2020-6-15 19:31
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GatewayPredicateDefinition {

    /**
     * 断言对应的Name
     */
    private String name;

    /**
     * 配置的断言规则
     */
    private Map<String, String> args = new LinkedHashMap<String, String>();

}
