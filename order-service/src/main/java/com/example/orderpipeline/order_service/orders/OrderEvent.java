package com.example.orderpipeline.order_service.orders;

import java.io.Serializable;

public class OrderEvent implements Serializable{

    private String orderId;
    private String sku;
    private int quantity;

    public OrderEvent() {
    }

    public OrderEvent(String orderId, String sku, int quantity) {
        this.orderId = orderId;
        this.sku = sku;
        this.quantity = quantity;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getSku() {
        return sku;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public void setSku(String sku) {
        this.sku = sku;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;   
    }

    @Override
    public String toString() {
        return "OrderEvent{" + "orderId='" + orderId + '\'' + ", sku='" + sku + '\'' + ", quantity=" + quantity + '}';
    }
}
