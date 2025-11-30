package com.example.orderpipeline.order_service.orders;

import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderProducer producer;
    private final Logger log = LoggerFactory.getLogger(OrderController.class);

    public OrderController(OrderProducer producer) {
        this.producer = producer;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> payload) {
        // basic validation
        if (!payload.containsKey("sku") || !payload.containsKey("qty")) {
            return ResponseEntity.badRequest().body(Map.of("error", "sku and qty required"));
        }

        String sku = String.valueOf(payload.get("sku"));
        int qty;
        try {
            qty = ((Number) payload.get("qty")).intValue();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(Map.of("error", "qty must be a number"));
        }

         String orderId = UUID.randomUUID().toString();
        OrderEvent ev = new OrderEvent(orderId, sku, qty);

        log.info("Received POST /orders -> {}", ev);
        producer.publish(ev);

        return ResponseEntity.accepted().body(Map.of("orderId", orderId));
    }
    
}
