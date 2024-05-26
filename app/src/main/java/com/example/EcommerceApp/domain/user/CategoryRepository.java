package com.example.EcommerceApp.domain.user;

import android.content.Context;

import com.example.EcommerceApp.model.Category;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {
    private FirebaseFirestore db;
    private CollectionReference categoriesCollection;

    public CategoryRepository(Context context){
        db = FirebaseFirestore.getInstance();
        categoriesCollection = db.collection("product-category");

    }
    public Task<List<Category>> getAllCategoriesAsList() {
        return categoriesCollection.get().continueWith(task -> {
            List<Category> categoryList = new ArrayList<>();
            if (task.isSuccessful()) {
                for (Category category : task.getResult().toObjects(Category.class)) {
                    categoryList.add(category);
                }
            }
            return categoryList;
        });
    }
}
