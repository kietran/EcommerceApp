package com.example.EcommerceApp.utils;

import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class FirebaseUtil {
    public static FirebaseUser currentUser(){return FirebaseAuth.getInstance().getCurrentUser();}
    public static void addFavorite(String productId, String userId) {
        Map<String, Object> favorite = new HashMap<>();
        favorite.put("product_id", productId);
        favorite.put("user_id", userId);

        FirebaseFirestore.getInstance().collection("test_favorite").add(favorite)
                .addOnSuccessListener(documentReference -> Log.d("FirebaseUtil", "Favorite added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("FirebaseUtil", "Error adding favorite", e));
    }

    public static void removeFavorite(String productId, String userId) {
        Query query = FirebaseFirestore.getInstance().collection("test_favorite")
                .whereEqualTo("product_id", productId)
                .whereEqualTo("user_id", userId);

        query.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            document.getReference().delete();
                        }
                    }
                });
    }
    public static String currentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }

    public static StorageReference getCurrentUserPicStorageRef(String path){
        return FirebaseStorage.getInstance().getReference().child(path)
                .child(FirebaseUtil.currentUserId());
    }

    public static StorageReference getCurrentShopPicStorageRef(String path, String shopId){
        return FirebaseStorage.getInstance().getReference().child(path)
                .child(shopId);
    }

    public static DocumentReference currentUserDetails(){
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }

    public static DocumentReference getShopReference(String shopId){
        return FirebaseFirestore.getInstance().collection("shop").document(shopId);
    }

    public static CollectionReference getAllProducts(){
        return FirebaseFirestore.getInstance().collection("test_product");
    }

    public static DocumentReference getOrderItemReference(String orderItem){
        return FirebaseFirestore.getInstance().collection("order_item").document(orderItem);
    }

    public static DocumentReference getProductReference(String productId){
        return FirebaseFirestore.getInstance().collection("test_product").document(productId);
    }
}
