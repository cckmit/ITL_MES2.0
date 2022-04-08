package com.itl.iap.notice.provider.core.kafka;

import com.itl.iap.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Map;

@ConditionalOnExpression("${notice.enableMq:true}")
@Component
@Slf4j
public class KafkaProducer {
    @Autowired
    @Qualifier("kafkaTemplate")
    KafkaTemplate kafkaTemplate ;
    @Value("${notice.stationTopics}")
    private String topics;
    public void sendMessage(Map<String, Object> notice){
        Assert.notNull(notice,"未传入消息对象");
        String topic = notice.get("topic") == null ? "" : notice.get("topic").toString();
        if(StringUtils.isEmpty(topic)){
            topic=topics;
        }
        String data = JsonUtils.deserializer(notice);
//        log.info("producer message,topic:"+topic);
//        Assert.notNull(topic,"未指定主题");
        kafkaTemplate.send(topic,data);
    }

}
