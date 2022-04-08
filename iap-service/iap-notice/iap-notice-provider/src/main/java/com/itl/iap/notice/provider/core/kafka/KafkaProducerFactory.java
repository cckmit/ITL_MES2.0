package com.itl.iap.notice.provider.core.kafka;

import org.springframework.kafka.core.DefaultKafkaProducerFactory;

import java.util.Map;

/**
 * kafka消息队列生产工厂
 *
 * @author 曾慧任
 * @date  2020-06-29
 * @since jdk1.8
 */
public class KafkaProducerFactory extends DefaultKafkaProducerFactory {
    public KafkaProducerFactory(Map config) {
        super(config);
    }
}
