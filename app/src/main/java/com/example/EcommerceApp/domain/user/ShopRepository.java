package com.example.EcommerceApp.domain.user;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.EcommerceApp.model.Product;
import com.example.EcommerceApp.model.Shop;
import com.example.EcommerceApp.utils.AndroidUtil;
import com.example.EcommerceApp.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopRepository {
    private Context context;
    private FirebaseFirestore db;
    private Timestamp timestamp;
    private CollectionReference shopCollection;
    private String shopId;

    public interface OnShopSaveListener {
        void onShopSaved(String shopId);
    }

    public ShopRepository(Context context) {
        shopId = null;
        db = FirebaseFirestore.getInstance();
        timestamp = Timestamp.now();
        shopCollection = db.collection("shop");
        this.context = context;
    }


    public String getShopId() {
        return shopId;
    }


    public void saveShop(String shopName, String phoneNumber, String shopAddress, String profileImage, String bannerImage, OnShopSaveListener listener){{
            Map<String, Object> shop = new HashMap<>();

            shop.put("userId", FirebaseUtil.currentUserId());
            shop.put("shopName", shopName);
            shop.put("phoneNumber", phoneNumber);
            shop.put("shopAddress", shopAddress);
            shop.put("profileImage", profileImage);
            shop.put("bannerImage", bannerImage);
            shop.put("createdTimestamp", timestamp);

            shopCollection.add(shop)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            String shopId = documentReference.getId(); // Retrieve the shop ID
                            AndroidUtil.showToast(context, "Create Successfully");
                            listener.onShopSaved(shopId); // Pass the shop ID to the listener
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            AndroidUtil.showToast(context, "Create Failed");
                        }
                    });
        }
    }

    public Task<List<Shop>> getAllShopAsList() {
        return shopCollection.get().continueWith(task -> {
            List<Shop> shopList = new ArrayList<>();
            if (task.isSuccessful()) {
                for (Shop shop : task.getResult().toObjects(Shop.class)) {
                    shopList.add(shop);
                }
            }
            return shopList;
        });
    }

}
