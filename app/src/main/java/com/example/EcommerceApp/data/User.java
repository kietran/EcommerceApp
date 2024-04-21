package com.example.EcommerceApp.data;
import com.google.firebase.Timestamp;
public class User {
    private String phone;
    private String username;
    private Timestamp createdTimestamp;
    private String address;
    private String email;
    private String password;

    public User() {
    }

    public User(String phone, String username, Timestamp createdTimestamp, String address, String email, String password) {
        this.phone = phone;
        this.username = username;
        this.createdTimestamp = createdTimestamp;
        this.email = email;
        this.password = password;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }
}
