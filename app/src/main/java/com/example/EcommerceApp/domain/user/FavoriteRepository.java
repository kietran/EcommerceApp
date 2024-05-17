package com.example.EcommerceApp.domain.user;

import android.content.Context;

import com.example.EcommerceApp.model.Category;
import com.example.EcommerceApp.model.Favorite;
import com.example.EcommerceApp.model.Product;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FavoriteRepository {
    private FirebaseFirestore db;
    private CollectionReference favoritesCollection;


    public FavoriteRepository(Context ccontext) {
        db = FirebaseFirestore.getInstance();
        favoritesCollection = db.collection("test_favorite");
    }
    // Phương thức để lấy toàn bộ danh sách các item từ Firestore


    public Task<List<Favorite>> getAllFavoriteAsListByUserID(String userID) {
        return favoritesCollection
                .whereEqualTo("user_id", userID)
                .get().continueWith(task -> {
                    List<Favorite> favoritesList = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            Favorite favorite = document.toObject(Favorite.class);
                            favoritesList.add(favorite);
                        }
                    }
                    return favoritesList;
                });
    }

}
