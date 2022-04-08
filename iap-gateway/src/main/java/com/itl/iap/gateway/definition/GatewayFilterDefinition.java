package com.itl.iap.gateway.definition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 路由过滤器实体
 *
 * @author 汤俊
 * @date 2020-6-15 19:30
 * @since jdk1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GatewayFilterDefinition {

    /**
     * Filter Name
     */
    private String name;

    /**
     * 对应的路由规则
     */
    private Map<String, String> args = new LinkedHashMap<String, String>();

}
