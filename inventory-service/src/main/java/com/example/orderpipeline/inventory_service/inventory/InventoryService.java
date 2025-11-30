package com.example.orderpipeline.inventory_service.inventory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private final Logger log = LoggerFactory.getLogger(InventoryService.class);

    private final Map<String, Integer> stock = new ConcurrentHashMap<>();

    private final Set<String> seenOrders = ConcurrentHashMap.newKeySet();
    
     public InventoryService() {
        // seed some initial stock for testing
        stock.put("item1", 100);
        stock.put("item2", 50);
        stock.put("item3", 200);
    }

    public synchronized boolean processOrder(OrderEvent ev) {
        if (ev == null) return false;
        String orderId = ev.getOrderId();
        if (orderId == null) return false;

        // idempotency: ignore if seen
        if (!seenOrders.add(orderId)) {
            log.info("Duplicate order ignored: {}", orderId);
            return false;
        }

        String sku = ev.getSku();
        int qty = ev.getQty();

        stock.compute(sku, (k, v) -> {
            if (v == null) {
                log.warn("SKU {} not present. Creating negative inventory.", sku);
                return -qty;
            } else {
                int newQty = v - qty;
                log.info("Reduced SKU {} by {} -> {}", sku, qty, newQty);
                return newQty;
            }
        });

        return true;
    }

    public Map<String, Integer> currentStock() {
        return Map.copyOf(stock);
    }

    public boolean hasSeen(String orderId) {
        return seenOrders.contains(orderId);
    }
}
