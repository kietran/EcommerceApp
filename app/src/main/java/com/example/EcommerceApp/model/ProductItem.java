package com.example.EcommerceApp.model;

public class ProductItem {
    String color;
    String product_id;
    String size;
    int qty_in_stock;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQty_in_stock() {
        return qty_in_stock;
    }

    public void setQty_in_stock(int qty_in_stock) {
        this.qty_in_stock = qty_in_stock;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;



    public ProductItem(String id, String color, String product_id, String size, int QTY) {
        this.id = id;
        this.color = color;
        this.product_id = product_id;
        this.size = size;
        this.qty_in_stock = QTY;
    }

    public ProductItem() {
    }
}

