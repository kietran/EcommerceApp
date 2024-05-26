package com.example.EcommerceApp.domain.user;


import android.content.Context;

import androidx.annotation.NonNull;

import com.example.EcommerceApp.model.ProductItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ProductItemRepository {
    private FirebaseFirestore db;
    private CollectionReference productitemsCollection;

    public ProductItemRepository(Context context) {
        db = FirebaseFirestore.getInstance();
        productitemsCollection = db.collection("product_item");
    }

    public Task<List<ProductItem>> getAllProductItemsAsListByProductID(String product_id) {
        return productitemsCollection
                .whereEqualTo("product_id", product_id)
                .get().continueWith(task -> {
                    List<ProductItem> productItemList = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            ProductItem productItem = document.toObject(ProductItem.class);
                            productItemList.add(productItem);
                        }
                    }
                    return productItemList;
                });
    }

    public Task<List<String>> getAllProductColorsAsListByProductID(String product_id) {
        return productitemsCollection
                .whereEqualTo("product_id", product_id)
                .get().continueWith(task -> {
                    List<String> productColors = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            ProductItem productItem = document.toObject(ProductItem.class);
                            String color = productItem.getColor();
                            productColors.add(color);
                        }
                    }
                    return productColors;
                });
    }

    public Task<List<String>> getAllProductSizesAsListByProductID(String product_id) {
        return productitemsCollection
                .whereEqualTo("product_id", product_id)
                .get().continueWith(task -> {
                    List<String> productSizes = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            ProductItem productItem = document.toObject(ProductItem.class);
                            String size = productItem.getSize();
                            productSizes.add(size);
                        }
                    }
                    return productSizes;
                });
    }

    public Task<Integer> getProductQTYByColorAndSize(String product_id, String color, String size) {
        return productitemsCollection
                .whereEqualTo("product_id", product_id)
                .whereEqualTo("color", color)
                .whereEqualTo("size", size)
                .get().continueWith(task -> {
                    int qty = 0;
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                        if (!documents.isEmpty()) {
                            DocumentSnapshot document = documents.get(0);
                            ProductItem productItem = document.toObject(ProductItem.class);
                            qty = productItem.getQty_in_stock();
                        }
                    }
                    return qty;
                });
    }

    public Task<ProductItem> getProductItemIdByColorAndSize(String product_id, String color, String size) {
        return productitemsCollection
                .whereEqualTo("product_id", product_id)
                .whereEqualTo("color", color)
                .whereEqualTo("size", size)
                .get().continueWith(task -> {
                    ProductItem productItem = new ProductItem();
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                        if (!documents.isEmpty()) {
                            DocumentSnapshot document = documents.get(0);
                            String id = document.getId();
                            productItem = document.toObject(ProductItem.class);
                            productItem.setId(id);
                        }
                    }
                    return productItem;
                });
    }

    public void updateQtyInStock(String productItemId, int newQty) {
        productitemsCollection.document(productItemId)
                .update("qty_in_stock", newQty)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("Quantity in stock updated successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý thất bại
                        System.err.println("Error updating quantity in stock: " + e.getMessage());
                    }
                });
    }

    public Task<Long> getQtyInStock(String productItemId) {
        DocumentReference docRef = productitemsCollection.document(productItemId);
        Task<DocumentSnapshot> task = docRef.get();

        return task.continueWith(task1 -> {
            if (task1.isSuccessful() && task1.getResult().exists()) {
                Long qtyInStock = task1.getResult().getLong("qty_in_stock");
                if (qtyInStock != null) {
                    return qtyInStock;
                } else {
                    throw new NullPointerException("Quantity in stock is null");
                }
            } else {
                throw task1.getException() != null ? task1.getException() : new Exception("Failed to get quantity in stock");
            }
        });
    }
}

