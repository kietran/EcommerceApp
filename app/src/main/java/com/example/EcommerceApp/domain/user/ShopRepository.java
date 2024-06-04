package com.example.EcommerceApp.domain.user;


import android.content.Context;

import androidx.annotation.NonNull;

import com.example.EcommerceApp.model.Shop;
import com.example.EcommerceApp.utils.AndroidUtil;
import com.example.EcommerceApp.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    public Task<Shop> getShopByShopID (String shopId) {
        DocumentReference docRef = shopCollection.document(shopId);
        return docRef.get().continueWith(task -> {
            Shop shop = new Shop();
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Assuming you have a constructor or method to convert a DocumentSnapshot to a Shop object
                    String id = document.getId();
                    shop = document.toObject(Shop.class);
                    shop.setShopId(id);
                }
            }
            return shop;
        });
    }
    public Task<String> getIdByName(String name) {
        return shopCollection.whereEqualTo("shopName", name).get().continueWith(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                return document.getId();
            } else {
                return null;
            }
        });
    }
    public Task<Shop> getShopByUserId() {
        return shopCollection.whereEqualTo("userId", FirebaseAuth.getInstance().getUid()).get().continueWith(task -> {
            Shop shop = null;
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (!querySnapshot.isEmpty()) {
                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                    shop = document.toObject(Shop.class);
                    Objects.requireNonNull(shop).setShopId(document.getId());
                }
            }
            return shop;
        });
    }
}
