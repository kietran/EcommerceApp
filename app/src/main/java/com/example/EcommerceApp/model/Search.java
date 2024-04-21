package com.example.EcommerceApp.model;

import com.google.firebase.Timestamp;

public class Search {
    private String id;
    private String user_id;
    private String content;
    private Timestamp timestamp;
    private int search_count;

    // Constructor
    public Search(String id, String userId, String content, Timestamp timestamp, int search_count) {
        this.id = id;
        this.user_id = userId;
        this.content = content;
        this.timestamp = timestamp;
        this.search_count = search_count;
    }
    public Search() {

    }


    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public int getSearch_count() {
        return search_count;
    }

    public void setSearch_count(int search_count) {
        this.search_count = search_count;
    }
}
