package com.itl.iap.common.base.interceptor;


import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * 返回的code统一管理
 *
 * @author 汤俊
 * @date 2020年6月9日
 * @since jdk1.8
 */
@Component
@Slf4j
public class FeignRequestInterceptor implements RequestInterceptor {

    /**
     * 对feign 请求手动的传递head
     *
     * @param requestTemplate
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        log.info("对feign 请求进行拦截");
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
                    requestTemplate.header(name, values);
                }
            }
        }
    }
}
