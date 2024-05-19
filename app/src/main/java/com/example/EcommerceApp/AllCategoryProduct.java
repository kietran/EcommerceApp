package com.example.EcommerceApp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.EcommerceApp.adapter.ProductAdapter;
import com.example.EcommerceApp.domain.user.FavoriteRepository;
import com.example.EcommerceApp.domain.user.ProductRepository;
import com.example.EcommerceApp.model.Category;
import com.example.EcommerceApp.model.Product;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AllCategoryProduct extends AppCompatActivity {
    ProductAdapter productAdapter;
    androidx.recyclerview.widget.RecyclerView viewCategory;
    ProductRepository productRepository;
    FavoriteRepository favoriteRepository;
    ImageView btBack;

    public AllCategoryProduct() {
        productRepository = new ProductRepository(this);
        favoriteRepository = new FavoriteRepository(this);
        productAdapter = new ProductAdapter(this, new ArrayList<>());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_category_product);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null){
            return;
        }
        Category category = (Category) bundle.get("object category");
        TextView categoryName = findViewById(R.id.category_name);
        categoryName.setText(category.getName());
        favoriteRepository.getAllFavoriteAsListByUserID(FirebaseAuth.getInstance().getCurrentUser().getUid()).addOnCompleteListener(task -> {
            List<String> favorites = task.getResult();
            productAdapter.setFavoriteProductID(favorites);});
        viewCategory = findViewById(R.id.viewCategory);
        productRepository.getAllProductsAsListByCategoryID(category.getId()).addOnCompleteListener(task -> {
            List<Product> products = task.getResult();
            productAdapter.updateData(products);
            GridLayoutManager layoutManagerProduct = new GridLayoutManager(this, 2);
            viewCategory.setLayoutManager(layoutManagerProduct);
            viewCategory.setAdapter(productAdapter);
        });
        btBack = findViewById(R.id.btBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AllCategoryProduct.this, UserActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}