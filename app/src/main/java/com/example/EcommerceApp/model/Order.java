package com.example.EcommerceApp.model;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.Map;

public class Order implements Serializable {
    String id;
    String shop;
    String status;

    public Timestamp getOrderAt() {
        return orderAt;
    }

    public void setOrderAt(Timestamp orderAt) {
        this.orderAt = orderAt;
    }

    Timestamp orderAt;
    Timestamp confirmAt;

    public Timestamp getConfirmAt() {
        return confirmAt;
    }

    public void setConfirmAt(Timestamp confirmAt) {
        this.confirmAt = confirmAt;
    }

    public Timestamp getDeliveryAt() {
        return deliveryAt;
    }

    public void setDeliveryAt(Timestamp deliveryAt) {
        this.deliveryAt = deliveryAt;
    }

    public Timestamp getCompleteAt() {
        return completeAt;
    }

    public void setCompleteAt(Timestamp completeAt) {
        this.completeAt = completeAt;
    }

    Timestamp deliveryAt;
    Timestamp completeAt;

    public Order() {
    }

    String customer_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public Map<String, Object> getAddress() {
        return address;
    }

    public void setAddress(Map<String, Object> address) {
        this.address = address;
    }

    Map<String,Object> address;

}
