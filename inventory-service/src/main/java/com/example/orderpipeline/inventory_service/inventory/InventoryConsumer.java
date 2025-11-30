package com.example.orderpipeline.inventory_service.inventory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class InventoryConsumer {

    private final Logger log = LoggerFactory.getLogger(InventoryConsumer.class);
    private final InventoryService inventoryService;

    public InventoryConsumer(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @KafkaListener(topics = "orders", groupId = "inventory-group", containerFactory = "kafkaListenerContainerFactory")
    public void onOrder(OrderEvent ev) {
        try {
            log.info("Consumed order event: {}", ev);
            boolean applied = inventoryService.processOrder(ev);
            if (applied) {
                log.info("Order applied: {}", ev.getOrderId());
            } else {
                log.info("Order ignored or invalid: {}", ev == null ? "null" : ev.getOrderId());
            }
        } catch (Exception ex) {
            log.error("Error processing event {} : {}", ev, ex.getMessage(), ex);
            // Production: push to DLQ topic or persistent failure queue
        }
    }

    
    
}
