package com.itl.mes.me.client.config;

import com.itl.mes.me.client.service.impl.FeignServiceImpl;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author yaoxiang
 * @date 2020/12/14
 * @since JDK1.8
 */
@Configuration
public class FallBackConfig {

    @Bean
    public FeignServiceImpl feignService() {
        return new FeignServiceImpl();
    }

    /**
     * feign远程调用丢失请求头问题
     *
     * @return
     */
    @Bean("requestInterceptor")
    public RequestInterceptor requestInterceptor() {
        return template -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames != null) {
                    while (headerNames.hasMoreElements()) {
                        String name = headerNames.nextElement();
                        String values = request.getHeader(name);
                        // 跳过 content-length
                        if (name.equals("content-length")) {
                            continue;
                        }
                        template.header(name, values);
                    }
                }
            }
        };
    }
}
