package com.example.EcommerceApp.domain.user;

import androidx.annotation.NonNull;

import com.example.EcommerceApp.model.OrderItem;
import com.example.EcommerceApp.model.ShoppingCartItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        orderItem1.setRated(false);
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
    public Task<Integer> getOrderItemCountByOrderId(String orderId) {
        return orderItem
                .whereEqualTo("order_id", orderId)
                .get()
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            return querySnapshot.size();
                        }
                    } else {
                        System.err.println("Error getting order items: " + task.getException());
                    }
                    return 0;
                });
    }
    public Task<List<OrderItem>> getOrderItemsByOrderId(String orderId) {
        return orderItem
                .whereEqualTo("order_id", orderId)
                .get()
                .continueWith(task -> {
                    List<OrderItem> orderItems = new ArrayList<>();
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                OrderItem orderItem = document.toObject(OrderItem.class);
                                orderItem.setId(document.getId());
                                orderItems.add(orderItem);
                            }
                        }
                    } else {
                        System.err.println("Error getting order items: " + task.getException());
                    }
                    return orderItems;
                });
    }
}
