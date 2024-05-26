package com.example.EcommerceApp.model;

public class ProductItem {
    String color;
    String size;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public long getQty_in_stock() {
        return qty_in_stock;
    }

    public void setQty_in_stock(long qty_in_stock) {
        this.qty_in_stock = qty_in_stock;
    }

    String product_id;
    long qty_in_stock;
    public ProductItem() {
    }
}
