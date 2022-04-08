package com.itl.iap.workflow.client.config;

import com.itl.iap.workflow.client.service.impl.ProcessFeignFallback;
import com.itl.iap.workflow.client.service.impl.TaskFeignFallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 远程调用配置类
 *
 * @author 黄建明
 * @date 2020-07-10
 * @since jdk1.8
 */
@Configuration
public class FallBackConfig {

    @Bean
    public TaskFeignFallback taskFeignClient() {
        return new TaskFeignFallback();
    }

    @Bean
    public ProcessFeignFallback processFeignClient() {
        return new ProcessFeignFallback();
    }


}
