package com.itl.iap.gateway.oauth.filter;

import com.itl.iap.gateway.oauth.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;

/**
 * Oauth2过滤器
 *
 * @author 汤俊
 * @date 2020-06-28
 * @since jdk1.8
 */
@Slf4j
@Component
public class Oauth2Filter implements GlobalFilter, Ordered {

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse response = exchange.getResponse();
        //TODO 过滤掉不需要进行权限控制的服务
        //获取token
        String accessToken = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (StringUtils.isNotBlank(accessToken)) {
            String[] tokens = accessToken.split(" ");
            //验证Access Token
            log.info("token ------->>>>" + tokens[1]);
            String userName = JWTUtil.getUsername(tokens[1]);
            if (!JWTUtil.verify(tokens[1], userName)) {
                // 如果不存在/过期了，返回未验证错误，需重新验证
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return Mono.empty();
            }
        }
        return chain.filter(exchange);
    }

}
