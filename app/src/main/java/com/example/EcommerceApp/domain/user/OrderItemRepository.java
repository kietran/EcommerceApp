package com.example.EcommerceApp.domain.user;

import androidx.annotation.NonNull;

import com.example.EcommerceApp.model.OrderItem;
import com.example.EcommerceApp.model.ShoppingCartItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class OrderItemRepository {
    private FirebaseFirestore db;
    private static CollectionReference orderItem;

    public OrderItemRepository() {
        db = FirebaseFirestore.getInstance();
        orderItem=db.collection("order_item");
    }
    public void create(String order_id, ShoppingCartItem cartItem)
    {
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setOrder_id(order_id);
        Map<String,Object> mapCartItem = new HashMap<>();
        mapCartItem.put("id",cartItem.getId());
        mapCartItem.put("qty",cartItem.getQty());
        Map<String,Object> product_item=cartItem.getProduct_item();
        mapCartItem.put("product_item",product_item);
        orderItem1.setCartItem(mapCartItem);
        orderItem.add(orderItem1).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        System.out.println("Order item added successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý thất bại, có thể hiển thị thông báo lỗi
                        System.err.println("Error adding order item: " + e.getMessage());
                    }
                });

    }
}
