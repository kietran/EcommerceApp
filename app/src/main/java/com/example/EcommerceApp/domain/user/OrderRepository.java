package com.example.EcommerceApp.domain.user;

import androidx.annotation.NonNull;

import com.example.EcommerceApp.model.Address;
import com.example.EcommerceApp.model.Order;
import com.example.EcommerceApp.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
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


}
