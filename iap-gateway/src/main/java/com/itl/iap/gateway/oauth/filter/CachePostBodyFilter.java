package com.itl.iap.gateway.oauth.filter;

import io.netty.buffer.UnpooledByteBufAllocator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.DefaultServerRequest;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * TODO
 * 缓存POST的Body过滤器
 *
 * @author 汤俊
 * @date 2020-7-2 19:25
 * @since jdk1.8
 */
@Slf4j
//@Component
public class CachePostBodyFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        String method = serverHttpRequest.getMethodValue();
        MediaType mediaType = serverHttpRequest.getHeaders().getContentType();
        if ("POST".equalsIgnoreCase(method)) {
            ServerRequest serverRequest = new DefaultServerRequest(exchange);
            Mono<String> bodyToMono = serverRequest.bodyToMono(String.class);
            if (MediaType.MULTIPART_FORM_DATA.isCompatibleWith(mediaType)) {
                return chain.filter(exchange);
            }
//            return  chain.filter(exchange);
            return bodyToMono.filter(body -> !body.isEmpty()).flatMap(body -> {
                exchange.getAttributes().put("cachedRequestBody", body);
                ServerHttpRequest newRequest = new ServerHttpRequestDecorator(serverHttpRequest) {
                    @Override
                    public HttpHeaders getHeaders() {
                        HttpHeaders httpHeaders = new HttpHeaders();
                        httpHeaders.putAll(super.getHeaders());
                        httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
                        return httpHeaders;
                    }

                    @Override
                    public Flux<DataBuffer> getBody() {
                        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(new UnpooledByteBufAllocator(false));
                        DataBuffer bodyDataBuffer = nettyDataBufferFactory.wrap(body.getBytes());
                        return Flux.just(bodyDataBuffer);
                    }
                };
                return chain.filter(exchange.mutate().request(newRequest).build());
            });
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -21;
    }

}
