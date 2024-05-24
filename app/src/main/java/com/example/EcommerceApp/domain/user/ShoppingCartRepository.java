package com.example.EcommerceApp.domain.user;

import android.content.Context;
import android.util.Log;

import com.example.EcommerceApp.model.ShoppingCart;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCartRepository {
    private FirebaseFirestore db;
    private CollectionReference cartCollection;

    public ShoppingCartRepository(Context ccontext) {
        db = FirebaseFirestore.getInstance();
        cartCollection = db.collection("shopping_cart");
    }
    public Task<String> createCart(String userId){
        Map<String, Object> shopping_cart = new HashMap<>();
        shopping_cart.put("user_id", userId);
        FirebaseFirestore.getInstance().collection("shopping_cart").add(shopping_cart)
                .addOnSuccessListener(documentReference -> Log.d("FirebaseUtil", "Shoppingcart added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("FirebaseUtil", "Error adding shopping_cart", e));
        return cartCollection
                .whereEqualTo("user_id", userId)
                .get().continueWith(task -> {
                    String cartID = null;
                    if(task.isSuccessful()){
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                        if (!documents.isEmpty()) {
                            DocumentSnapshot document = documents.get(0);
                            cartID = document.getId();
                        }
                    }
                    return cartID;
                });
    }
    public Task<String> getCartId(String userID){
        return cartCollection
                .whereEqualTo("user_id", userID)
                .get().continueWith(task -> {
                    String cartID = null;
                    if(task.isSuccessful()){
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                        if (!documents.isEmpty()) {
                            DocumentSnapshot document = documents.get(0);
                            cartID = document.getId();
                        }
                    }
                    return cartID;
                });
    }
    public Task<ShoppingCart> getShoppingCartByUserID(String userID){
        return cartCollection
                .whereEqualTo("user_id", userID)
                .get().continueWith(task -> {
                    ShoppingCart cart = new ShoppingCart();
                    if(task.isSuccessful()){
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                        if (!documents.isEmpty()) {
                            DocumentSnapshot document = documents.get(0);
                            String id = document.getId();
                            cart = document.toObject(ShoppingCart.class);
                            cart.setId(id);
                        }
                    }
                    return cart;
                });
    }
}
