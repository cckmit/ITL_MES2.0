package com.itl.iap.common.base.config;

import com.itl.iap.common.base.interceptor.IMomBaseRequestInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * config
 *
 * @author linjl
 * @date 2021-2-3
 */
@Configuration
public class IMomWebConfig extends WebMvcConfigurationSupport {
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        /**
         * 拦截器按照顺序执行,如果不同拦截器拦截存在相同的URL，前面的拦截器会执行，后面的拦截器将不执行
         */
        registry.addInterceptor(new IMomBaseRequestInterceptor())
                .addPathPatterns();
        super.addInterceptors(registry);
    }
}

