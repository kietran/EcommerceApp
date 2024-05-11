package com.example.EcommerceApp.model;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.EcommerceApp.R;

public class Category{
    String category_image;
    String name;
    int numProduct;

    public Category(){}

    public Category(String category_image, String name, int numProduct) {
        this.category_image = category_image;
        this.name = name;
        this.numProduct = numProduct;
    }

    public String getCategory_image() {
        return category_image;
    }

    public void setCategory_image(String category_image) {
        this.category_image = category_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumProduct() {
        return numProduct;
    }

    public void setNumProduct(int numProduct) {
        this.numProduct = numProduct;
    }
}