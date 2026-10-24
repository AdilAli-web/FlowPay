package com.example.FlowPay.Kafka;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
@ConditionalOnProperty(name="app.kafka-enabled",havingValue = "true")
public class OutBoxPublisher {
    private final EventRepo repo;
    private final KafkaTemplate<String,String> kafka;

    public OutBoxPublisher(EventRepo repo, KafkaTemplate<String, String> kafka) {
        this.repo = repo;
        this.kafka = kafka;
    }


    @Transactional
    public void publish(String id) throws Exception
    {
        Event e=repo.lockById(id).orElseThrow();
        if(e.publishedAt!=null) return;
        kafka.send(KafkaConfig.TOPIC,e.merchantId,e.merchantId).get(12, TimeUnit.SECONDS);
        e.publishedAt= Instant.now();
    }

}
