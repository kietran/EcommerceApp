package com.example.EcommerceApp.domain.user;

import android.content.Context;

import com.example.EcommerceApp.model.Product;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private FirebaseFirestore db;
    private CollectionReference productsCollection;


    public ProductRepository(Context ccontext) {
        db = FirebaseFirestore.getInstance();
        productsCollection = db.collection("test_product");
    }
    // Phương thức để lấy toàn bộ danh sách các item từ Firestore


    public Task<List<Product>> getAllProductsAsList() {
        return productsCollection.get().continueWith(task -> {
            List<Product> productsList = new ArrayList<>();
            if (task.isSuccessful()) {
                for (Product product : task.getResult().toObjects(Product.class)) {
                    productsList.add(product);
                }
            }
            return productsList;
        });
    }
    public Task<List<Product>> getAllProductByNamesAsList(String name) {
        return productsCollection
                .whereArrayContains("name", name)
                .get().continueWith(task -> {
            List<Product> productsList = new ArrayList<>();
            if (task.isSuccessful()) {
                for (Product product : task.getResult().toObjects(Product.class)) {
                    productsList.add(product);
                }
            }
            return productsList;
        });
    }
//    public Task<QuerySnapshot> getAllProducts() {
//        return productsCollection.get();
//    }

//    public Task<Void> addItem(Item item) {
//        return productsCollection.document().set(item);
//    }
//
//    public Task<Void> removeItem(String itemId) {
//        return productsCollection.document(itemId).delete();
//    }
//
//    public Task<Void> updateItem(String itemId, Item updatedItem) {
//        return productsCollection.document(itemId).set(updatedItem);
//    }
}
