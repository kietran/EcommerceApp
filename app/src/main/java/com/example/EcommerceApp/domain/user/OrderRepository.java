package com.example.EcommerceApp.domain.user;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.EcommerceApp.model.Address;
import com.example.EcommerceApp.model.Order;
import com.example.EcommerceApp.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderRepository {
    private FirebaseFirestore db;
    private static CollectionReference order;

    public OrderRepository() {
        db = FirebaseFirestore.getInstance();
        order=db.collection("order");
    }
    public Task<String> create(String shopName, String status, Address address, Timestamp orderAt) {
        TaskCompletionSource<String> taskCompletionSource = new TaskCompletionSource<>();

        Order order1 = new Order();
        order1.setShop(shopName);
        order1.setCustomer_id(FirebaseUtil.currentUserId());
        order1.setStatus(status);

        Map<String, Object> mapAddress = new HashMap<>();
        mapAddress.put("phone", address.getPhone());
        mapAddress.put("receiver_name", address.getReceiver_name());
        mapAddress.put("province", address.getProvince());
        mapAddress.put("district", address.getDistrict());
        mapAddress.put("ward", address.getWard());
        mapAddress.put("address_line", address.getAddress_line());
        order1.setAddress(mapAddress);
        order1.setOrderAt(orderAt);

        order.add(order1)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Trả về ID của document
                        taskCompletionSource.setResult(documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Trả về lỗi
                        taskCompletionSource.setException(e);
                    }
                });

        return taskCompletionSource.getTask();
    }

    public Task<List<Order>> getOrdersWithStatusNotComplete() {
        return order
                .whereEqualTo("customer_id", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereNotEqualTo("status", "complete")
                .get()
                .continueWith(task -> {
                    List<Order> orders = new ArrayList<>();
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                Order order = document.toObject(Order.class);
                                order.setId(document.getId());
                                orders.add(order);
                            }
                        }
                    } else {
                        System.err.println("Error getting orders: " + task.getException());
                    }
                    return orders;
                });
    }

    public Task<List<Order>> getOrdersWithStatusComplete() {
        return order
                .whereEqualTo("customer_id", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .whereEqualTo("status", "complete")
                .get()
                .continueWith(task -> {
                    List<Order> orders = new ArrayList<>();
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                Order order = document.toObject(Order.class);
                                order.setId(document.getId());
                                orders.add(order);
                            }
                        }
                        Log.i("size order", String.valueOf(orders.size()));
                    } else {
                        System.err.println("Error getting orders: " + task.getException());
                    }
                    return orders;
                });
    }

    public Task<Void> updateOrderStatus(String orderId, String status) {
        TaskCompletionSource<Void> taskCompletionSource = new TaskCompletionSource<>();

        // Create the update map
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", status);

        updates.put(status+"At", Timestamp.now());

        order.document(orderId)
                .update(updates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Notify task completion source of success
                        taskCompletionSource.setResult(null);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Notify task completion source of failure
                        taskCompletionSource.setException(e);
                    }
                });

        return taskCompletionSource.getTask();
    }

}
