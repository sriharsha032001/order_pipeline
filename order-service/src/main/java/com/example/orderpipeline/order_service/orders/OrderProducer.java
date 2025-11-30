package com.example.orderpipeline.order_service.orders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class OrderProducer {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;
    private final Logger log = LoggerFactory.getLogger(OrderProducer.class);
    private final String topic = "orders";

    public OrderProducer(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(OrderEvent event) {
        // use SKU as key so all items for same SKU go to same partition
        String key = event.getSku();
        kafkaTemplate.send(topic, key, event).addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(org.springframework.kafka.support.SendResult<String, OrderEvent> result) {
                log.info("Published order event: {} to partition {}", event, result.getRecordMetadata().partition());
            }
            @Override
            public void onFailure(Throwable ex) {
                log.error("Failed to publish order event: {}", event, ex);
            }
        });
    }
    
}
