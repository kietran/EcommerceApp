package com.example.EcommerceApp.model;

import java.util.Map;

public class ShoppingCartItem
{
    private Map<String,Object> cart;
    private  Map<String,Object> product_item;
    private Map<String,Object> shop;
    private String id;

    private int qty;

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    private boolean available;

    public Map<String, Object> getShop() {
        return shop;
    }

    public void setShop(Map<String, Object> shop) {

        this.shop = shop;
    }

    public ShoppingCartItem() {

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }




    @Override
    public String toString() {
        return "ShoppingCartItem{" +
                "id='" + id + '\'' +
                ", qty=" + qty +
                '}';
    }

    public Map<String, Object> getCart() {
        return cart;
    }

    public void setCart(Map<String, Object> cart) {
        this.cart = cart;
    }

    public Map<String, Object> getProduct_item() {
        return product_item;
    }

    public void setProduct_item(Map<String, Object> product_item) {
        this.product_item = product_item;
    }
}
