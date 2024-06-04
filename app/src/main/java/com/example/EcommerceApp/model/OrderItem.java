package com.example.EcommerceApp.model;

import java.util.Map;

public class OrderItem {
    String id;
    String order_id;
    Map<String,Object> cartItem;
    boolean isRated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public Map<String, Object> getCartItem() {
        return cartItem;
    }

    public void setCartItem(Map<String, Object> cartItem) {
        this.cartItem = cartItem;
    }

    public boolean isRated() {
        return isRated;
    }

    public void setRated(boolean rated) {
        isRated = rated;
    }
}
