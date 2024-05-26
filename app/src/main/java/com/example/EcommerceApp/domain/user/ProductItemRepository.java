package com.example.EcommerceApp.domain.user;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProductItemRepository {
    private FirebaseFirestore db;
    private static CollectionReference productItem;

    public ProductItemRepository() {
        db = FirebaseFirestore.getInstance();
        productItem=db.collection("product_item");
    }
    public void updateQty(String productItemId,int qty){

    }
    public Task<Long> getQtyInStock(String productItemId) {
        DocumentReference docRef = productItem.document(productItemId);
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

    public void updateQtyInStock(String productItemId, int newQty) {
        productItem.document(productItemId)
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
}
