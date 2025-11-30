package com.example.orderpipeline.inventory_service.inventory;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/inventory")
    public Map<String, Integer> getStock() {
        return inventoryService.currentStock();
    }

    @GetMapping("/inventory/seen/{orderId}")
    public boolean seen(@PathVariable String orderId) {
        return inventoryService.hasSeen(orderId);
    }
    
}
