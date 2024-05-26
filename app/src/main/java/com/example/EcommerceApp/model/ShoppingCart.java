package com.example.EcommerceApp.model;

public class ShoppingCart {
    String userID;
    String id;

    public ShoppingCart(){}
    public ShoppingCart(String userID) {
        this.userID = userID;
    }

    public ShoppingCart(String userID, String id) {
        this.userID = userID;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
